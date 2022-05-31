package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

public class MatchmakingServer extends Server {
    private static final int wellKnownPort = 50000;
    private final Vector<GameServer> startedGames = new Vector<>();

    private final List<Thread> gameThreads = new Vector<>();

    private final MainSavesManager savesManager = new MainSavesManager(logger);

    int getPortToBind() {
        return wellKnownPort;
    }

    @Override
    void onStart() {
        if(!savesManager.createSavesFolder()) {
            stop();
        }
        List<SavedGameRecord> records = savesManager.restoreAll();
        for (SavedGameRecord r : records) {
            GameServer server = new GameServer(r.getGame(), r.getUsernames(), r.getSavesManager());
            runGameServer(server);
        }
    }

    void moveToGame(User user, GameServer game) {
        user.getConnection().send(new Redirect(game.getPort()));
        String toLog = "Moving user " + user.getName() + " to game " + game + " with port " + game.getPort();
        logger.log(Level.INFO, toLog);
    }

    // create a thread that waits for the game to finish and disconnects all the related users
    void runGameServer(GameServer server) {
        Thread t = new Thread(() -> {
            server.run();
            List<String> usernames = server.usernames();
            List<User> connectedCopy = new ArrayList<>(getConnectedUsers());
            for (User u : connectedCopy) {
                if (usernames.contains(u.getName())) {
                    disconnectUser(u);
                }
            }
        });
        addGameServer(server, t);
        t.start();
    }

    synchronized void addGameServer(GameServer server, Thread t) {
        startedGames.add(server);
        gameThreads.add(t);
    }

    @Override
    synchronized void onQuit() {
        for (GameServer game: getStartedGames()) {
            game.stop();
        }
        for (Thread thread: gameThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    void onUserReconnected(User user) {
        for (GameServer g : getStartedGames()) {
            if (g.getAssignedUsernames().contains(user.getName())) {
                // if user is already connected directly refuse new connection
                for (User u: g.connectedUsers) {
                    GameUser gu = (GameUser) u;
                    if (gu.getName().equals(user.getName()) && !gu.isDisconnected()) {
                        user.getConnection().send(new ErrorMessage());
                        break;
                    }
                }
                // this part runs if user is disconnected or if is assigned but never joined
                user.getConnection().send(new Redirect(g.getPort()));
                break;
            }
        }
    }

    @Override
    void onNewUserConnect(User user, Login info) {
        user.getConnection().bindFunction(this::onLobbyAction);
        List<LobbyDescriptor> lobbies = new ArrayList<>();
        for (GameServer g: getStartedGames()) {
            List<String> connected = g.getAssignedUsernames();
            if (connected.size() < g.maxUsers)
                lobbies.add(new LobbyDescriptor(g.getCode(), g.getPlayerNumber(), g.getGameMode(), connected));
        }
        user.getConnection().send(new LobbiesList(lobbies));
    }

    boolean onLobbyAction(Connection connection) {
        Message message = connection.getLastMessage();
        User user = userFromConnection(connection);
        if (message instanceof LobbyChoice lobbyChoice) {
            for (GameServer g : getStartedGames()) {
                if (g.getCode().equals(lobbyChoice.getCode())) {
                    if (g.assignUser(user.getName()))  // if lobby is full returns false and sends ErrorMessage
                        moveToGame(user, g);
                    else
                        connection.send(new ErrorMessage());
                    return true;
                }
            }
            // no matching lobby found for this code
            connection.send(new ErrorMessage());
            return true;
        } else if (message instanceof CreateLobby createLobby) {
            if (createLobby.getGameMode() == null || createLobby.getPlayerNumber() == null)
                return true;  // skip this message and do nothing because mal formatted
            GameSavesManager sm = savesManager.createGameSavesManager();
            GameServer game = new GameServer(createLobby.getPlayerNumber(), createLobby.getGameMode(), sm);
            runGameServer(game);
            game.assignUser(user.name);
            moveToGame(user, game);
            return true;
        } else if (message instanceof Disconnected disconnected) {
            disconnectUser(user);
            return true;
        }
        return false;
    }

    List<GameServer> getStartedGames() {
        return startedGames;
    }
}

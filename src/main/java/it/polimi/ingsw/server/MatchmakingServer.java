package it.polimi.ingsw.server;

import it.polimi.ingsw.networking.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MatchmakingServer extends Server {
    public static final int WELL_KNOWN_PORT = 50000;
    private final List<GameServer> startedGames = new SafeList<>();

    private final List<Thread> gameThreads = new SafeList<>();

    private final MainSavesManager savesManager = new MainSavesManager(logger);

    @Override
    int getPortToBind() {
        return WELL_KNOWN_PORT;
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
            deleteGameServer(server);
        });
        addGameServer(server, t);
        t.start();
    }

    synchronized void deleteGameServer(GameServer server) {
        int index = startedGames.indexOf(server);
        startedGames.remove(index);
        gameThreads.remove(index);
    }

    synchronized void addGameServer(GameServer server, Thread t) {
        startedGames.add(server);
        gameThreads.add(t);
    }

    @Override
    void onQuit() {
        List<GameServer> sg;
        List<Thread> gt;
        synchronized (this) {
            sg = getStartedGames();
            gt = getGameThreads();
        }
        for (GameServer game: sg) {
            game.stop();
        }
        for (Thread thread: gt) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    void onUserReconnected(User user) {
        for (GameServer g : getStartedGames()) {  // if user is already connected properly refuse new connection
            for (User u: g.connectedUsers) {
                GameUser gu = (GameUser) u;
                if (gu.getName().equals(user.getName()) && !gu.isDisconnected()) {
                    user.getConnection().send(new ErrorMessage());  // tell to client that this name is already taken
                    user.getConnection().close(); // don't remove the user, just close the connection
                    return;
                }
            }
        }
        // after handling the refuse treat exactly as a new user
        onNewUserConnect(user);
    }

    @Override
    void onNewUserConnect(User user) {
        for (GameServer g : getStartedGames()) {
            if (g.getAssignedUsernames().contains(user.getName())) {
                user.getConnection().send(new Redirect(g.getPort()));
                return;
            }
        }
        // this part runs if the user is assigned to no game (means he quit while in lobby selection)
        // he should be treated as a new user
        user.getConnection().bindFunction(this::onLobbyAction);
        user.getConnection().send(getLobbiesList());
    }

    LobbiesList getLobbiesList() {
        List<LobbyDescriptor> lobbies = new ArrayList<>();
        for (GameServer g: getStartedGames()) {
            List<String> connected = g.getAssignedUsernames();
            if (connected.size() < g.maxUsers)
                lobbies.add(new LobbyDescriptor(g.getCode(), g.getPlayerNumber(), g.getGameMode(), connected));
        }
        return new LobbiesList(lobbies);
    }

    boolean onLobbyAction(Connection connection) {
        Message message = connection.getLastMessage();
        User user = userFromConnection(connection);
        if (message instanceof LobbyChoice lobbyChoice) {
            for (GameServer g : getStartedGames()) {
                if (g.getCode().equals(lobbyChoice.code())) {
                    if (g.assignUser(user.getName()))  // if lobby is full returns false and sends ErrorMessage
                        moveToGame(user, g);
                    else
                        connection.send(getLobbiesList());
                    return true;
                }
            }
            // no matching lobby found for this code
            connection.send(getLobbiesList());
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
        }
        return false;
    }

    synchronized List<GameServer> getStartedGames() {
        return new ArrayList<>(startedGames);
    }

    synchronized List<Thread> getGameThreads() {
        return new ArrayList<>(gameThreads);
    }

    public synchronized boolean noGamesRunning() {
        return gameThreads.isEmpty() && startedGames.isEmpty();
    }
}

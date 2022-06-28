package it.polimi.ingsw.server;

import it.polimi.ingsw.networking.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * the server responsible for redirecting the users to the correct game server and creating a new one when requested
 */
public class MatchmakingServer extends Server {
    public static final int WELL_KNOWN_PORT = 50000;
    private final List<GameServer> startedGames = new SafeList<>();

    private final List<Thread> gameThreads = new SafeList<>();

    private final MainSavesManager savesManager = new MainSavesManager(logger);

    /**
     * use a wellknown port to allow clients to connect
     * @return wellknown port number
     */
    @Override
    int getPortToBind() {
        return WELL_KNOWN_PORT;
    }

    /**
     * on server start restore saved games
     */
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

    /**
     * redirect client to game server
     * @param user the user to send the redirect message to
     * @param game the game server to redirect the user to
     */
    void moveToGame(User user, GameServer game) {
        user.getConnection().send(new Redirect(game.getPort()));
        String toLog = "Moving user " + user.getName() + " to game " + game + " with port " + game.getPort();
        logger.log(Level.INFO, toLog);
    }

    /**
     * create a thread that waits for the game to finish and disconnects all the related users when terminated
     * @param server the game server to run
     */
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

    /**
     * remove game server and corresponding thread from startedGames and gameThreads
     * @param server server game to remove
     */
    synchronized void deleteGameServer(GameServer server) {
        int index = startedGames.indexOf(server);
        startedGames.remove(index);
        gameThreads.remove(index);
    }

    /**
     * add game server and corresponding thread to startedGames and gameThreads
     * @param server server game to add
     * @param t thread running the server to add to the list
     */
    synchronized void addGameServer(GameServer server, Thread t) {
        startedGames.add(server);
        gameThreads.add(t);
    }

    /**
     * when server is terminating close all the game servers and wait to the end of their threads
     */
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

    /**
     * check that the user lost connection before accepting a reconnection
     * @param user the user who reconnected
     */
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

    /**
     * add new user and bind callback function for lobby action
     * or redirect to game server if user has a game to reconnect to
     * @param user the user who connected
     */
    @Override
    void onNewUserConnect(User user) {
        if(user.name.equals("")) {
            user.getConnection().send(new ErrorMessage());  // tell to client that this name is not available
            user.getConnection().close(); // don't remove the user, just close the connection
            return;
        }
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

    /**
     * get information from lobbies and group in a list
     * @return LobbiesList message containing information about available lobbies
     */
    LobbiesList getLobbiesList() {
        List<LobbyDescriptor> lobbies = new ArrayList<>();
        for (GameServer g: getStartedGames()) {
            List<String> connected = g.getAssignedUsernames();
            if (connected.size() < g.maxUsers)
                lobbies.add(new LobbyDescriptor(g.getCode(), g.getPlayerNumber(), g.getGameMode(), connected));
        }
        return new LobbiesList(lobbies);
    }

    /**
     * a callback function that moves the user to an existing game server in case of LobbieChoice
     * or creates a new lobby for the user in case of CreateLobby
     * if LobbieChoice is not valid send the updated LobbiesList
     * @param connection the connection from which the message is coming from
     * @return if the message was processed and therefore should be consumed
     */
    boolean onLobbyAction(Connection connection) {
        Message message = connection.getFirstMessage();
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

    /**
     * get a copy of startedGames
     * @return list of running game servers
     */
    synchronized List<GameServer> getStartedGames() {
        return new ArrayList<>(startedGames);
    }

    /**
     * get a copy of gameThreads
     * @return list of threads running game servers
     */
    synchronized List<Thread> getGameThreads() {
        return new ArrayList<>(gameThreads);
    }

    /**
     * check both gameThreads and startedGames are empty
     * @return if no game is running
     */
    public synchronized boolean noGamesRunning() {
        return gameThreads.isEmpty() && startedGames.isEmpty();
    }
}

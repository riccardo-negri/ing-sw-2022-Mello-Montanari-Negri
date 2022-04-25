package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Redirect;

import java.util.ArrayList;
import java.util.List;

public class MatchmakingServer extends Server {
    private static final int wellKnownPort = 50000;
    private final List<GameServer> startedGames;

    private final List<Thread> gameThreads;

    public MatchmakingServer() {
        startedGames = new ArrayList<>();
        gameThreads = new ArrayList<>();
    }

    int portToBind() {
        return wellKnownPort;
    }

    @Override
    void onStart() {

    }

    static void moveToGame(User user, GameServer game) {
        game.assignUser(user.getName());
        user.getConnection().send(new Redirect(game.getPort()));
        System.out.println("Moving user " + user.getName() + " to game " + game + " with port " + game.getPort());
    }

    // create a thread that waits for the game to finish and disconnects all the related users
    void runGameServer(GameServer server) {
        Thread t = new Thread(() -> {
            server.run();
            List<String> usernames = server.usernames();
            List<User> connectedCopy = new ArrayList<>(getConnectedUser());
            for (User u : connectedCopy) {
                if (usernames.contains(u.getName())) {
                    disconnectUser(u);
                }
            }
        });
        synchronized (this) {
            startedGames.add(server);
            gameThreads.add(t);
        }
        t.start();
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
            synchronized (g.getAssignedUsernames()) {
                if (g.getAssignedUsernames().contains(user.getName())) {
                    user.getConnection().send(new Redirect(g.getPort()));
                }
            }
        }
    }

    // if game with desired parameters doesn't exist create it and redirect the user to that game server
    @Override
    void onNewUserConnect(User user, Login info) {
        for (GameServer g : getStartedGames()) {
            synchronized (g.getAssignedUsernames()) {
                if (!g.isFull()) {
                    if (g.getMaxUsers() == info.getPlayerNumber() && g.isAdvancedRules() == info.isAdvancedRules()) {
                        moveToGame(user, g);
                        return;
                    }
                }
            }
        }
        // reach this point only if no compatible game exists
        GameServer game = new GameServer(info.getPlayerNumber(), info.isAdvancedRules());
        runGameServer(game);
        moveToGame(user, game);
    }

    public synchronized List<GameServer> getStartedGames() {
        return new ArrayList<>(startedGames);
    }
}

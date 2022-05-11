package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MatchmakingServer extends Server {
    private static final int wellKnownPort = 50000;
    private final Vector<GameServer> startedGames;

    private final List<Thread> gameThreads;

    public MatchmakingServer() {
        startedGames = new Vector<>();
        gameThreads = new Vector<>();
    }

    int getPortToBind() {
        return wellKnownPort;
    }

    @Override
    void onStart() {

    }

    static void moveToGame(User user, GameServer game) {
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
                user.getConnection().send(new Redirect(g.getPort()));
            }
        }
    }

    // if game with desired parameters doesn't exist create it and redirect the user to that game server
    @Override
    void onNewUserConnect(User user, Login info) {
        for (GameServer g : getStartedGames()) {
            if (g.getPlayerNumber() == info.getPlayerNumber() && g.getGameMode() == info.getGameMode()) {
                if (g.assignUser(user.getName())) {
                    moveToGame(user, g);
                    return;
                }
            }
        }
        // reach this point only if no compatible game exists
        GameServer game = new GameServer(info.getPlayerNumber(), info.getGameMode());
        runGameServer(game);
        game.assignUser(user.name);
        moveToGame(user, game);
    }

    List<GameServer> getStartedGames() {
        return startedGames;
    }
}

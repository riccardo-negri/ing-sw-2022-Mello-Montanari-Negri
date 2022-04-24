package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Redirect;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MatchmakingServer extends Server {
    private static final int wellKnownPort = 60000;
    private final List<GameServer> startedGames;

    private final List<Thread> gameThreads;


    public MatchmakingServer() {
        startedGames = new ArrayList<>();
        gameThreads = new ArrayList<>();
    }

    @Override
    void openSocket() throws IOException {
        socket = new ServerSocket(wellKnownPort);
    }

    @Override
    void onStart() {

    }

    static void moveToGame(User user, GameServer game) {
        game.assignUser(user.getName());
        user.getConnection().send(new Redirect(game.getPort()));
    }

    // create a thread that waits for the game to finish and disconnects all the related users
    void runGameServer(GameServer server) {
        startedGames.add(server);
        Thread t = new Thread(() -> {
            server.run();
            List<String> usernames = server.usernames();
            List<User> connectedCopy = new ArrayList<>(connectedUser);
            for (User u : connectedCopy) {
                if (usernames.contains(u.getName())) {
                    u.getConnection().close();
                    connectedUser.remove(u);
                }
            }
        });
        t.start();
        gameThreads.add(t);
    }

    @Override
    void onQuit() {
        for (GameServer game: startedGames) {
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
        for (GameServer g : startedGames) {
            if (g.getAssignedUsernames().contains(user.getName())) {
                moveToGame(user, g);
            }
        }
    }

    // if game with desired parameters doesn't exist create it and redirect the user to that game server
    @Override
    void onNewUserConnect(User user, Login info) {
        for (GameServer g : startedGames) {
            if(!g.isFull()) {
                if (g.getMaxUsers() == info.getPlayerNumber() && g.isAdvancedRules() == info.isAdvancedRules()) {
                    moveToGame(user, g);
                    return;
                }
            }
        }
        // reach this point only if no compatible game exists
        GameServer game = new GameServer(info.getPlayerNumber(), info.isAdvancedRules());
        runGameServer(game);
        moveToGame(user, game);
    }
}

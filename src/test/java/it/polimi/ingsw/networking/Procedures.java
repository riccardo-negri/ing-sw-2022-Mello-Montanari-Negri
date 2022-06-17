package it.polimi.ingsw.networking;

import it.polimi.ingsw.server.MatchmakingServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Procedures {

    static List<Connection> twoBoundedConnections(Logger logger) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(MatchmakingServer.WELL_KNOWN_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Connection> result = new ArrayList<>();
        Thread t = new Thread(() -> {
            try {
                Connection c = new Connection(new SafeSocket(serverSocket.accept()), logger);
                synchronized (result) {
                    result.add(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        Connection c = new Connection("localhost", MatchmakingServer.WELL_KNOWN_PORT, logger);
        synchronized (result) {
            result.add(c);
        }
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

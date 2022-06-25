package it.polimi.ingsw.networking;

import it.polimi.ingsw.server.MatchmakingServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Procedures {

    static List<Connection> twoBoundedConnections(Logger logger) {
        return twoBoundedConnectionsWithFunction(logger, null);
    }

    static List<Connection> twoBoundedConnectionsWithFunction(Logger logger, Predicate<Connection> pc) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Connection> result = new ArrayList<>();
        Thread t = new Thread(() -> {
            try {
                SafeSocket s = new SafeSocket(serverSocket.accept());
                Connection c;
                if (pc == null)
                    c = new Connection(s, logger);
                else
                    c = new Connection(s, pc, logger);
                synchronized (result) {
                    result.add(c);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        Connection c;
        if (pc == null)
            c = new Connection("localhost", serverSocket.getLocalPort(), logger);
        else
            c = new Connection("localhost", serverSocket.getLocalPort(), pc, logger);
        synchronized (result) {
            result.add(c);
        }
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

package it.polimi.ingsw.server;


import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.SafeSocket;
import it.polimi.ingsw.utils.LogFormatter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Server {
    protected final List<Connection> connecting;

    // users in this vector stay there also if the socket stops working for network issues
    // when they reconnect the new connection is used for the same user
    protected final UniqueUserList connectedUsers;
    protected int maxUsers = Integer.MAX_VALUE;
    protected ServerSocket socket;
    protected int port;

    protected final Logger logger;

    int getPortToBind() {
        return 0;
    }

    // initialize variables but don't run server code yet
    protected Server() {
        connectedUsers = new UniqueUserList();
        connecting = new SafeList<>();  // using SafeList instead of ArrayList because SafeList class is thread-safe
        logger = LogFormatter.getLogger("Server");
        int attempts = 0;
        do {
            try {
                attempts++;
                socket = new ServerSocket(getPortToBind());
                port = socket.getLocalPort();
            } catch (IOException e) {
                String toLog = "Unable to open server socket: " + e.getMessage();
                logger.log(Level.SEVERE, toLog);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e2) {
                    logger.log(Level.WARNING, "Interrupted", e2);
                    Thread.currentThread().interrupt();
                }
            }
        } while (socket == null && attempts <= 10);
    }

    List<String> usernames() {
        return getConnectedUsers().stream().map(User::getName).toList();
    }

    abstract void onStart();

    abstract void onQuit();

    public void stop() {
        try {
            socket.close(); // this should stop the connectionThread and cause the termination of the server
        } catch (IOException ignored) { /*ignored*/ }
    }

    // open the socket and run server code
    // this method blocks the caller until server terminates
    public void run() {
        Thread connectionThread;
        connectionThread = new Thread(this::listenConnection);
        connectionThread.start();
        onStart();
        try {
            connectionThread.join(); // waits here until this.stop() is called
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Interrupted", e);
            connectionThread.interrupt();
        }
        for (Connection c : getConnecting()) {
            c.close();
        }
        for (User u : getConnectedUsers()) {
            u.getConnection().close();
        }
        onQuit();
    }

    // listen for new connections until socket.close() is used
    // in that case socket.accept() throws SocketException, and we terminate the thread
    void listenConnection() {
        String toLog = "Listening for new connections on port: " + getPort();
        logger.log(Level.INFO, toLog);
        boolean running = true;
        while (running) {
            try {
                SafeSocket acceptedSocket = new SafeSocket(this.socket.accept());
                toLog = "Accepted new connection from: " + acceptedSocket.getInetAddress();
                logger.log(Level.INFO, toLog);
                connecting.add(new Connection(acceptedSocket, this::userLogin, logger));
            } catch (SocketException e) {
                running = false;
            } catch (IOException ignored) { /*ignored*/ }
        }
    }

    abstract void onUserReconnected(User user);

    abstract void onNewUserConnect(User user);

    boolean isUserAllowed(Login info) {
        return true;
    }

    public boolean userLogin(Connection source) {
        Login login;
        try {
            login = (Login) source.getLastMessage();
        } catch (ClassCastException e) {
            abortConnection(source);
            return false;
        }
        if(isUserAllowed(login)) {
            connectNewUser(source, login);
        }
        else {
            abortConnection(source);
        }
        return true;
    }

    void connectNewUser(Connection connection, Login login) {
        User user = createUser(login.username(), connection);
        if(connectedUsers.addWithLimit(user, maxUsers)) {
            connecting.remove(connection);
            String toLog = "New user logged in: " + user.getName();
            logger.log(Level.INFO, toLog);
            onNewUserConnect(user);
        }
        else {
            reconnectUser(connection, login);
        }
    }

    void reconnectUser(Connection connection, Login login) {
        for (User u : getConnectedUsers()) {
            if (u.getName().equals(login.username())) {
                u.replaceConnection(connection);
                connecting.remove(connection);
                String toLog = "User " + login.username() + " reconnected";
                logger.log(Level.INFO, toLog);
                onUserReconnected(u);
            }
        }
    }
    
    User createUser(String name, Connection connection) {
        return new User(name, connection);
    }

    public User userFromConnection(Connection connection) {
        for (User u: connectedUsers) {
            if (u.getConnection().equals(connection)) {
                return u;
            }
        }
        return null;
    }

    public void abortConnection(Connection connection) {
        connection.close();
        connecting.remove(connection);
    }

    public void disconnectUser(User user) {
        user.getConnection().close();
        connectedUsers.remove(user);
    }

    public List<User> getConnectedUsers() {
        return connectedUsers;
    }

    public List<Connection> getConnecting() {
        return connecting;
    }

    // everyone joined at least once, we don't know if they disconnected later
    public boolean isEveryoneConnected() {
        return connectedUsers.size() >= maxUsers;
    }

    public int getPort() {
        return port;
    }
}

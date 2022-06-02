package it.polimi.ingsw.server;


import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.SafeSocket;
import it.polimi.ingsw.utils.LogFormatter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Server {
    // using Vector instead of ArrayList because Vector class is thread-safe
    protected final Vector<Connection> connecting;

    // users in this vector stay there also if the socket stops working for network issues
    // when they reconnect the new connection is used for the same user
    protected final UniqueUserVector connectedUsers;
    protected int maxUsers = Integer.MAX_VALUE;
    protected ServerSocket socket;
    protected int port;

    protected final Logger logger;

    int getPortToBind() {
        return 0;
    }

    // initialize variables but don't run server code yet
    public Server() {
        connectedUsers = new UniqueUserVector();
        connecting = new Vector<>();
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
            }
        } while (socket == null && attempts <= 3);
    }

    List<String> usernames() {
        return getConnectedUsers().stream().map(User::getName).collect(Collectors.toList());
    }

    abstract void onStart();

    abstract void onQuit();

    public void stop() {
        try {
            socket.close(); // this should stop the connectionThread and cause the termination of the server
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            String toLog = "Interrupted: " + e.getMessage();
            logger.log(Level.WARNING, toLog);
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
                SafeSocket socket;
                socket = new SafeSocket(this.socket.accept());
                toLog = "Accepted new connection from: " + socket.getInetAddress();
                logger.log(Level.INFO, toLog);
                connecting.add(new Connection(socket, this::userLogin, logger));
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    running = false;
                }
            }
        }
    }

    abstract void onUserReconnected(User user);

    abstract void onNewUserConnect(User user, Login info);

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
        User user = createUser(login.getUsername(), connection);
        if(connectedUsers.addWithLimit(user, maxUsers)) {
            connecting.remove(connection);
            String toLog = "New user logged in: " + user.getName();
            logger.log(Level.INFO, toLog);
            onNewUserConnect(user, login);
        }
        else {
            reconnectUser(connection, login);
        }
    }

    void reconnectUser(Connection connection, Login login) {
        for (User u : getConnectedUsers()) {
            if (u.getName().equals(login.getUsername())) {
                u.replaceConnection(connection);
                connecting.remove(connection);
                String toLog = "User " + login.getUsername() + " reconnected";
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

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getPort() {
        return port;
    }
}

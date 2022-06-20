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

    /**
     * return port 0 which means to bind socket to any available port
     * this method can be overwritten to use a different port in subclasses
     * @return the port to bind the socket to
     */
    int getPortToBind() {
        return 0;
    }

    /**
     * initialize server variables but don't run server code yet
     */
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
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e2) {
                    logger.log(Level.WARNING, "Interrupted", e2);
                    Thread.currentThread().interrupt();
                }
            }
        } while (socket == null && attempts <= 20);
    }

    /**
     * combine in a list the usernames of the connected users
     * @return a list of strings which contains connected usernames
     */
    List<String> usernames() {
        return getConnectedUsers().stream().map(User::getName).toList();
    }

    /**
     * this method can be extended to add actions when server starts
     */
    abstract void onStart();

    /**
     * this method can be extended to add actions when server is terminating
     */
    abstract void onQuit();

    /**
     * close the socket and terminates the serve by causing an exception on socket.accept()
     */
    public void stop() {
        try {
            socket.close(); // this should stop the connectionThread and cause the termination of the server
        } catch (IOException ignored) { /*ignored*/ }
    }

    /**
     * open the socket and run server code
     * this method blocks the caller until server terminates
     */
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

    /**
     * listen for new connections until socket.close() is used
     * in that case socket.accept() throws SocketException, and we end the thread execution
     */
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

    /**
     * this method can be extended to add actions when user reconnects
     * @param user the user who reconnected
     */
    abstract void onUserReconnected(User user);

    /**
     * this method can be extended to add actions when user connects for the first time
     * @param user the user who connected
     */
    abstract void onNewUserConnect(User user);

    /**
     * this method can be extended to limit access only to certain users
     * @param info login contains the username sent from the client which is available before accepting the user
     * @return if the new user is accepted from the server
     */
    boolean isUserAllowed(Login info) {
        return true;
    }

    /**
     * process the user login message and is used as a callback for new connections
     * @param source the connection from which the login message comes
     * @return if the message was processed
     */
    public boolean userLogin(Connection source) {
        Login login;
        try {
            login = (Login) source.getFirstMessage();
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

    /**
     * if is a new user creates the User object else run reconnectUser()
     * @param connection the connection to the user client
     * @param login the login information containing the username
     */
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

    /**
     * attach the new connection to an already existing user
     * @param connection the new connection to the user client
     * @param login the login information containing the username
     */
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

    /**
     * give name and connection create the user object
     * can be extended from subclasses to specialize the type of user created
     * @param name the username of the user
     * @param connection the connection to the user client
     * @return the new user object
     */
    User createUser(String name, Connection connection) {
        return new User(name, connection);
    }

    /**
     * given the connection find the user among all the connected users
     * @param connection the connection belonging to the user
     * @return the user containing the given connection, null if not present, should always be present
     */
    public User userFromConnection(Connection connection) {
        for (User u: connectedUsers) {
            if (u.getConnection().equals(connection)) {
                return u;
            }
        }
        return null;
    }

    /**
     * close connection and remove it from the connecting list because was rejected by the server
     * @param connection the connection to close
     */
    public void abortConnection(Connection connection) {
        // no error message because only a malevolent client can reach this situation (connecting to forbidden server)
        connection.close();
        connecting.remove(connection);
    }

    /**
     * close user connection and remove the user form connectedUser list
     * @param user the user to disconnect
     */
    public void disconnectUser(User user) {
        user.getConnection().close();
        connectedUsers.remove(user);
    }

    /**
     * get connectedUsers value
     * @return the list of all the user currently connected to the server
     */
    public List<User> getConnectedUsers() {
        return connectedUsers;
    }

    /**
     * get connecting value
     * @return the list of connections on which the server is waiting for login information
     */
    public List<Connection> getConnecting() {
        return connecting;
    }

    /**
     * is used only on a server with limitate capacity
     * check if everyone joined at least once and therefore the server is full, we don't know if they disconnected later
     * @return if everyone is connected
     */
    public boolean isEveryoneConnected() {
        return connectedUsers.size() >= maxUsers;
    }

    /**
     * get port value
     * @return the port of the socket listening for new connections
     */
    public int getPort() {
        return port;
    }
}

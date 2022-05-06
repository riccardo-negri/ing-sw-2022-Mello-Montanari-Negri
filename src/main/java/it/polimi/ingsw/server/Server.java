package it.polimi.ingsw.server;


import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.SafeSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public abstract class Server {
    // using Vector instead of ArrayList because Vector class is thread-safe
    protected final Vector<Connection> connecting;
    protected final UniqueUserVector connectedUser;
    protected int maxUsers = Integer.MAX_VALUE;
    protected ServerSocket socket;
    protected int port;

    int getPortToBind() {
        return 0;
    }

    // initialize variables but don't run server code yet
    public Server() {
        connectedUser = new UniqueUserVector();
        connecting = new Vector<>();
        try {
            socket = new ServerSocket(getPortToBind());
            port = socket.getLocalPort();
        } catch (IOException e) {
            System.out.println("Unable to open server socket:");
            System.out.println(e.getMessage());
        }
    }

    List<String> usernames() {
        return getConnectedUser().stream().map(User::getName).collect(Collectors.toList());
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
            throw new RuntimeException(e);
        }
        for (Connection c : getConnecting()) {
            c.close();
        }
        for (User u : getConnectedUser()) {
            u.getConnection().close();
        }
        onQuit();
    }

    // listen for new connections until socket.close() is used
    // in that case socket.accept() throws SocketException, and we terminate the thread
    void listenConnection() {
        System.out.println("Listening for new connections on port: " + getPort());
        while (true) {
            try {
                SafeSocket socket;
                socket = new SafeSocket(this.socket.accept());
                System.out.println("Accepted new connection from: " + socket.getInetAddress());
                connecting.add(new Connection(socket, this::userLogin));
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    return;
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
        if(connectedUser.addWithLimit(user, maxUsers)) {
            connecting.remove(connection);
            System.out.println("New user logged in: " + user.getName());
            onNewUserConnect(user, login);
        }
        else {
            reconnectUser(connection, login);
        }
    }

    void reconnectUser(Connection connection, Login login) {
        for (User u : getConnectedUser()) {
            if (u.getName().equals(login.getUsername())) {
                u.replaceConnection(connection);
                connecting.remove(connection);
                System.out.println("User " + login.getUsername() + " reconnected");
                onUserReconnected(u);
            }
        }
    }
    
    User createUser(String name, Connection connection) {
        return new User(name, connection);
    }

    public User userFromConnection(Connection connection) {
        for (User u: connectedUser) {
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
        connectedUser.remove(user);
    }

    public List<User> getConnectedUser() {
        return connectedUser;
    }

    public List<Connection> getConnecting() {
        return connecting;
    }

    public boolean isEveryoneConnected() {
        return connectedUser.size() >= maxUsers;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getPort() {
        return port;
    }
}

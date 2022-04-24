package it.polimi.ingsw.server;


import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Server {
    protected final List<Connection> connecting;
    protected final List<User> connectedUser;
    protected int maxUsers = Integer.MAX_VALUE;
    protected ServerSocket socket;
    protected final Object socketLock;

    // initialize variables but don't run server code yet
    public Server() {
        connectedUser = new ArrayList<>();
        connecting = new ArrayList<>();
        socketLock = new Object();
        try {
            openSocket();
        } catch (IOException e) {
            System.out.println("Unable to open server socket:");
            System.out.println(e.getMessage());
        }
    }

    List<String> usernames() {
        return getConnectedUser().stream().map(User::getName).collect(Collectors.toList());
    }

    void openSocket() throws IOException {
        socket = new ServerSocket();
    }

    abstract void onStart();

    abstract void onQuit();

    public void stop() {
        try {
            synchronized (socketLock) {
                socket.close(); // this should stop the connectionThread and cause the termination of the server
            }
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
        while (true) {
            try {
                Socket socket;
                synchronized (socketLock) {
                    socket = this.socket.accept();
                }
                synchronized (connecting) {
                    connecting.add(new Connection(socket, this::userLogin));
                }
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    return;
                }
            }
        }
    }

    abstract void onUserReconnected(User user);

    abstract void onNewUserConnect(User user, Login info);

    public void userLogin(Message message) {
        assert (message.getSource().isPresent());
        Connection connection = message.getSource().get();
        Login login;
        try {
            login = (Login) message;
        } catch (ClassCastException e) {
            connection.close();
            removeConnecting(connection);
            return;
        }
        if (usernames().contains(login.getUsername())) {
            for (User u : getConnectedUser()) {
                if (u.getName().equals(login.getUsername())) {
                    u.replaceConnection(connection);
                    removeConnecting(connection);
                    onUserReconnected(u);
                    return;
                }
            }
        } else {
            if (getConnectedUser().size() < maxUsers) {
                User user = new User(login.getUsername(), connection);
                synchronized (connectedUser) {
                    connectedUser.add(user);
                }
                removeConnecting(connection);
                onNewUserConnect(user, login);
            }
        }
    }

    public void disconnectUser(User user) {
        synchronized (connectedUser) {
            user.getConnection().close();
            connectedUser.remove(user);
        }
    }

    public void removeConnecting(Connection connection) {
        synchronized (connecting) {
            connecting.remove(connection);
        }
    }

    public List<User> getConnectedUser() {
        synchronized (connectedUser) {
            return new ArrayList<>(connectedUser);
        }
    }

    public List<Connection> getConnecting() {
        synchronized (connecting) {
            return new ArrayList<>(connecting);
        }
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getPort() {
        synchronized (socketLock) {
            return socket.getLocalPort();
        }
    }
}

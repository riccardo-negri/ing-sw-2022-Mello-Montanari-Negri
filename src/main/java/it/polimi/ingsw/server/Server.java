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

    // initialize variables but don't run it.polimi.ingsw.server code yet
    public Server() {
        connectedUser = new ArrayList<>();
        connecting = new ArrayList<>();
        try {
            openSocket();
        } catch (IOException e) {
            System.out.println("Unable to open it.polimi.ingsw.server socket:");
            System.out.println(e.getMessage());
        }
    }

    List<String> usernames() {
        return connectedUser.stream().map(User::getName).collect(Collectors.toList());
    }

    void openSocket() throws IOException {
        socket = new ServerSocket();
    }

    abstract void onStart();

    abstract void onQuit();

    public void stop() {
        try {
            socket.close(); // this should stop the connectionThread and cause the termination of the it.polimi.ingsw.server
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // open the socket and run the it.polimi.ingsw.server code
    // this method blocks the caller until the it.polimi.ingsw.server terminates
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
        for (Connection c : connecting) {
            c.close();
        }
        for (User u : connectedUser) {
            u.getConnection().close();
        }
        onQuit();
    }

    // listen for new connections until socket.close() is used
    // in that case socket.accept() throws SocketException, and we terminate the thread
    void listenConnection() {
        while (true) {
            try {
                Socket socket = this.socket.accept();
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

    public void userLogin(Message message) {
        assert (message.getSource().isPresent());
        Connection connection = message.getSource().get();
        Login login;
        try {
            login = (Login) message;
        } catch (ClassCastException e) {
            connection.close();
            connecting.remove(connection);
            return;
        }
        if (usernames().contains(login.getUsername())) {
            for (User u : connectedUser) {
                if (u.getName().equals(login.getUsername())) {
                    u.replaceConnection(connection);
                    connecting.remove(connection);
                    onUserReconnected(u);
                    return;
                }
            }
        } else {
            if (connectedUser.size() < maxUsers) {
                User user = new User(login.getUsername(), connection);
                connectedUser.add(user);
                connecting.remove(connection);
                onNewUserConnect(user, login);
            }
        }
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public int getPort() {
        return socket.getLocalPort();
    }
}

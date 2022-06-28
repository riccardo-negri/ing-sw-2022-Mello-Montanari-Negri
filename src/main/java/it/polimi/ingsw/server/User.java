package it.polimi.ingsw.server;


import it.polimi.ingsw.networking.Connection;

/**
 * the class that represents the user on the server, can survive the disconnection, a new connection can be assinged
 * to the same user when reconnecting
 */
public class User {
    protected Connection connection;

    protected final String name;

    /**
     * associates a username and a connection
     * @param name the username of this user
     * @param connection the connection to the user client
     */
    public User(String name, Connection connection) {
        this.connection = connection;
        this.name = name;
    }

    /**
     * replace the connection, usually because the user disconnected and reconnected to the server
     * @param newConnection the new connection to use
     */
    public synchronized void replaceConnection(Connection newConnection) {
        this.connection.close();
        this.connection = newConnection;
    }

    /**
     * get name value
     * @return the username of this user
     */
    public String getName() {
        return name;
    }

    /**
     * get connection value
     * @return the connection currently in use
     */
    public synchronized Connection getConnection() {
        return connection;
    }
}

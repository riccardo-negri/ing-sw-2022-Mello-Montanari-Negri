package it.polimi.ingsw.server;


import it.polimi.ingsw.networking.Connection;

public class User {
    protected Connection connection;

    protected final String name;

    public User(String name, Connection connection) {
        this.connection = connection;
        this.name = name;
    }

    public synchronized void replaceConnection(Connection newConnection) {
        this.connection.close();
        this.connection = newConnection;
    }
    
    public String getName() {
        return name;
    }

    public synchronized Connection getConnection() {
        return connection;
    }
}

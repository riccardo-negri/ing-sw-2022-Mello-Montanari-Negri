package it.polimi.ingsw.server;


import it.polimi.ingsw.utils.Connection;

public class User {
    private Connection connection;
    private final Wizard wizard;

    private final String name;

    public User(String name, Connection connection) {
        this.name = name;
        wizard = new Wizard();
    }

    public void replaceConnection(Connection newConnection) {
        this.connection.close();
        this.connection = newConnection;
    }
    
    public String getName() {
        return name;
    }

    public Connection getConnection() {
        return connection;
    }
}
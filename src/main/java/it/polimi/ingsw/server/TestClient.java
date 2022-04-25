package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.*;

public class TestClient {
    Connection connection;
    Login login = new Login("tommaso", 3, true);

    public static void main(String[] args) {
        new TestClient();
    }

    public TestClient() {
        connection = new Connection("localhost", 50000);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage();
        System.out.println("porta");
        System.out.println(redirect.getPort());
        connection.close();
        connection = new Connection("localhost", redirect.getPort());
        connection.send(login);
        connection.close();
    }
}

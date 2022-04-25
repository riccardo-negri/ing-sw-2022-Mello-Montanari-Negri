package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestClient {
    Connection connection;
    Login login;

    public static void main(String[] args) throws IOException {
        new TestClient();
    }

    public TestClient() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name = reader.readLine();
        login = new Login(name, 2, true);
        connection = new Connection("localhost", 50000);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage();
        System.out.println("porta");
        System.out.println(redirect.getPort());
        connection.close();
        connection = new Connection("localhost", redirect.getPort());
        connection.send(login);
        connection.waitMessage(GameStart.class);
        connection.close();
    }
}

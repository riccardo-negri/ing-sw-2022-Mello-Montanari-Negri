package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.*;

public class Client {
    Connection connection;
    Login login = new Login("tommaso", 3, true);

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        connection = new Connection("localhost", 50000, this::onRedirect);
        connection.send(login);
    }

    void onRedirect(ReceivedMessage message) {
        Redirect redirect = (Redirect) message.getContent();
        System.out.println("porta");
        System.out.println(redirect.getPort());
        connection.stop();
        connection = new Connection("localhost", redirect.getPort(), this::onMessage);
        connection.send(login);
        connection.close();
    }

    void onMessage(ReceivedMessage message) {

    }
}

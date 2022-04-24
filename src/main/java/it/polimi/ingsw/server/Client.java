package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.*;

public class Client {
    Connection connection;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        connection = new Connection("localhost", 50000, this::onRedirect);
        connection.send(new Login("tommaso", 3, true));
    }

    void onRedirect(ReceivedMessage message) {
        Redirect redirect = (Redirect) message.getContent();
        System.out.println("porta");
        System.out.println(redirect.getPort());
        connection.close();
        connection = new Connection("localhost", redirect.getPort(), this::onMessage);
    }

    void onMessage(ReceivedMessage message) {

    }
}

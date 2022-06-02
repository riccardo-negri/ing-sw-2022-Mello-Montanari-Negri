package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;

public class EriantysClient {
    public static void main(String[] args) {
        Client client = new Client(true);
        client.start();
    }

}

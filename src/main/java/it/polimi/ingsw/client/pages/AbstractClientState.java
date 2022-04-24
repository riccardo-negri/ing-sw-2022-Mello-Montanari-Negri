package it.polimi.ingsw.client.pages;

import it.polimi.ingsw.client.Client;

public abstract class AbstractClientState {
    protected Client client;

    public AbstractClientState (Client client) {
        this.client = client;
    }

    public abstract void draw(Client client);

}

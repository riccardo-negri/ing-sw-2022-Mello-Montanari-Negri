package it.polimi.ingsw.client.pages;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.pages.ClientState.START_PAGE;

public abstract class AbstractWelcomePage extends AbstractClientState {

    public AbstractWelcomePage (Client client) {
        super(client);
    }

    public void onEnd () {
        client.setNextState(START_PAGE);
    }
}

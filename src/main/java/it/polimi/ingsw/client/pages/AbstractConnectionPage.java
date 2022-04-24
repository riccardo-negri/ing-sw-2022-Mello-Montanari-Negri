package it.polimi.ingsw.client.pages;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.pages.ClientState.BOARD_PAGE;
import static it.polimi.ingsw.client.pages.ClientState.WELCOME_PAGE;

public abstract class AbstractConnectionPage extends AbstractClientState{
    public AbstractConnectionPage (Client client) {
        super(client);
    }

    public void attemptConnection() {

    }

    public void onEnd() {
        client.setNextState(BOARD_PAGE);
    }
}

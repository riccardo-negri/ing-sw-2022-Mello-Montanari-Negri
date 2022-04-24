package it.polimi.ingsw.client.pages;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.pages.ClientState.*;

public abstract class AbstractStartPage extends AbstractClientState {

    public AbstractStartPage (Client client) {
        super(client);
    }

    public void onEnd (Integer n) {
        switch (n) {
            case 1:
                client.setNextState(CONNECTION_PAGE);
                break;
            default:
                client.setNextState(WELCOME_PAGE);
        }
    }
}

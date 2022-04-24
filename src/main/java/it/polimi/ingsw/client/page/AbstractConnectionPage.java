package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientState.GAME_MODE_SELECTION_PAGE;

public abstract class AbstractConnectionPage extends AbstractClientState{
    public AbstractConnectionPage (Client client) {
        super(client);
    }

    public void attemptConnection() {

    }

    public void onEnd() {
        client.setNextState(GAME_MODE_SELECTION_PAGE);
    }
}

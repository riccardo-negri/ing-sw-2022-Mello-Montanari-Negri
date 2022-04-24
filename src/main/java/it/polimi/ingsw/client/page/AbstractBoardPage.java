package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientState.START_PAGE;

public abstract class AbstractBoardPage extends AbstractClientState {

    public AbstractBoardPage (Client client) {
        super(client);
    }

    public void onEnd () {
        client.setNextState(START_PAGE);
    }
}

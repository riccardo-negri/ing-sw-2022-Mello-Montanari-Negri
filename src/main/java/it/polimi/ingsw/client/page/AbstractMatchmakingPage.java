package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientState.BOARD_PAGE;

public abstract class AbstractMatchmakingPage extends AbstractClientState {
    public AbstractMatchmakingPage (Client client) {
        super(client);
    }

    public void onEnd() {
        client.setNextState(BOARD_PAGE);
    }
}

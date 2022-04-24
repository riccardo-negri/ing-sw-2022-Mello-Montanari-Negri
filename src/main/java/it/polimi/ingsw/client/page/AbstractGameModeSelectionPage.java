package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientState.MATCHMAKING_PAGE;

public abstract class AbstractGameModeSelectionPage extends AbstractClientState {
    public AbstractGameModeSelectionPage (Client client) {
        super(client);
    }

    public void onEnd() {
        client.setNextState(MATCHMAKING_PAGE);
    }
}

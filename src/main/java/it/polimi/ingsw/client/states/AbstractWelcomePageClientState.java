package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.states.ClientState.START_PAGE;

public abstract class AbstractWelcomePageClientState extends AbstractClientState {

    public AbstractWelcomePageClientState (Client client) {
        super(client);
    }

    public void onEnd() {
        client.setNextState(START_PAGE);
    }
}

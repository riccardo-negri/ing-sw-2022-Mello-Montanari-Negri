package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CoreCLI;

import static it.polimi.ingsw.client.states.ClientState.START_PAGE;
import static it.polimi.ingsw.client.states.ClientState.WELCOME_PAGE;

public abstract class AbstractStartPageClientState extends AbstractClientState {

    public AbstractStartPageClientState (Client client) {
        super(client);
    }

    public void onEnd() {
        client.setNextState(WELCOME_PAGE);
    }
}

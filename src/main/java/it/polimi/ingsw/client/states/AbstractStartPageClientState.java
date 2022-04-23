package it.polimi.ingsw.client.states;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CoreCLI;

import static it.polimi.ingsw.client.states.ClientState.*;

public abstract class AbstractStartPageClientState extends AbstractClientState {

    public AbstractStartPageClientState (Client client) {
        super(client);
    }

    public void onEnd (Integer n) {
        switch (n) {
            case 1:
                client.setNextState(BOARD_PAGE);
                break;
            default:
                client.setNextState(WELCOME_PAGE);
        }
    }
}

package it.polimi.sweng.client;

import it.polimi.sweng.client.states.ClientState;
import it.polimi.sweng.client.ui.UI;
import it.polimi.sweng.client.ui.cli.CoreCLI;
import it.polimi.sweng.client.ui.cli.UtilsCLI;

public class Client {
    private final UI ui;
    private ClientState nextState;
    private boolean newState;

    public Client(boolean hasGUI) {
        if(hasGUI) {
            ui = null;
        }
        else {
            ui = new CoreCLI();
        }
        nextState = ClientState.WELCOME_PAGE;
        newState = true;
    }

    public void start() {
        UtilsCLI.test();
    }

    public ClientState getNextState () {
        return nextState;
    }

    public void setNextState (ClientState nextState) {
        this.nextState = nextState;
    }

}

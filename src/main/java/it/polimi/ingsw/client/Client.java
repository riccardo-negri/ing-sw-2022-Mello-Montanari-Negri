package it.polimi.ingsw.client;

import it.polimi.ingsw.client.states.ClientState;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CoreCLI;
import it.polimi.ingsw.client.ui.cli.UtilsCLI;
import it.polimi.ingsw.client.ui.cli.WelcomePageClientStateCLI;

public class Client {
    private final UI ui;
    private ClientState nextState;
    private boolean newState;
    private WelcomePageClientStateCLI temp;

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
        temp = new WelcomePageClientStateCLI();
        temp.draw();
        UtilsCLI.test();
    }

    public ClientState getNextState () {
        return nextState;
    }

    public void setNextState (ClientState nextState) {
        this.nextState = nextState;
    }

}

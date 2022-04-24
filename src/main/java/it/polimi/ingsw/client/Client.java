package it.polimi.ingsw.client;

import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CoreCLI;
import it.polimi.ingsw.client.ui.cli.WelcomePageCLI;

public class Client {
    private final UI ui;
    private ClientState nextState;
    private boolean newState;
    private WelcomePageCLI temp;
    private AbstractClientState currState;

    public Client(boolean hasGUI) {
        if(hasGUI) {
            ui = null;
        }
        else {
            ui = new CoreCLI();
        }
        nextState = ClientState.BOARD_PAGE;
        newState = true;
    }

    public void start() {
        while(nextState != null) {
            currState = ui.getState(this, nextState);
            currState.draw(this);
        }
    }

    public ClientState getNextState () {
        return nextState;
    }

    public void setNextState (ClientState nextState) {
        this.nextState = nextState;
    }

    public UI getUI () {
        return ui;
    }

}

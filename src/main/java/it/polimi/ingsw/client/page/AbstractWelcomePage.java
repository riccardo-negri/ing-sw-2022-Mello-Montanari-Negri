package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

public abstract class AbstractWelcomePage extends AbstractPage {

    public AbstractWelcomePage (Client client) {
        super(client);
    }

    public void onEnd (boolean isSupported) {
        if(isSupported) {
            client.setNextState(MENU_PAGE);
        }
        else {
            client.setNextState(null);
        }
    }
}

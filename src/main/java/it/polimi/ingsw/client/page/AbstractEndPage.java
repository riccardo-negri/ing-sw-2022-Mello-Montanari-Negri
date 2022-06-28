package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

/**
 * abstract page for the END state to be extended in the UI implementation
 */
public abstract class AbstractEndPage extends AbstractPage {

    protected AbstractEndPage (Client client) {
        super(client);
    }

    /**
     * set next state to MENU_PAGE
     */
    public void onEnd () {
        client.setNextState(MENU_PAGE);
    }
}

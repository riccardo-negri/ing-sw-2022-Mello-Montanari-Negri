package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

/**
 * abstract page for the WELCOME state to be extended in the UI implementation
 */
public abstract class AbstractWelcomePage extends AbstractPage {

    protected AbstractWelcomePage (Client client) {
        super(client);
    }

    /**
     * set next state to MENU_PAGE or if the screen is too small set it to null
     * @param isSupported false if the screen is not big enough for the CLI
     */
    public void onEnd (boolean isSupported) {
        if (isSupported) {
            client.setNextState(MENU_PAGE);
        }
        else {
            client.setNextState(null);
        }
    }
}

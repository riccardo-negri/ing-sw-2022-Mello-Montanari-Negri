package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.*;

/**
 * abstract page for the MENU state to be extended in the UI implementation
 */
public abstract class AbstractMenuPage extends AbstractPage {

    protected AbstractMenuPage (Client client) {
        super(client);
    }

    /**
     * set next state to CONNECTION_PAGE or STORYLINE_PAGE or CREDITS_PAGE or null or MENU_PAGE
     * @param n number representing next page selection
     */
    public void onEnd (Integer n) {
        switch (n) {
            case 1 -> client.setNextState(CONNECTION_PAGE);
            case 2 -> client.setNextState(STORYLINE_PAGE);
            case 3 -> client.setNextState(CREDITS_PAGE);
            case 4 -> client.setNextState(null);
            default -> client.setNextState(MENU_PAGE);
        }
    }
}

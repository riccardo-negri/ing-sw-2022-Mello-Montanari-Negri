package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

/**
 * abstract page for the STORY_LINE state to be extended in the UI implementation
 */
public abstract class AbstractStorylinePage extends AbstractPage {

    protected AbstractStorylinePage (Client client) {
        super(client);
    }

    /**
     * set next state to MENU_PAGE
     */
    public void onEnd () {
        client.setNextState(MENU_PAGE);
    }
}

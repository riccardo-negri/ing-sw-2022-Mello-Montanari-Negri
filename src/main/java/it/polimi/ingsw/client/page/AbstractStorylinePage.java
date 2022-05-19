package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

public abstract class AbstractStorylinePage extends AbstractPage {

    protected AbstractStorylinePage (Client client) {
        super(client);
    }

    public void onEnd () {
        client.setNextState(MENU_PAGE);
    }
}

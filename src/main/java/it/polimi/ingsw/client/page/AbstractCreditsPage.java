package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

public abstract class AbstractCreditsPage extends AbstractPage {

    protected AbstractCreditsPage (Client client) {
        super(client);
    }

    public void onEnd () {
        client.setNextState(MENU_PAGE);
    }
}

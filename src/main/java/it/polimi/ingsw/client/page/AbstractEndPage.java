package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.START_PAGE;

public abstract class AbstractEndPage extends AbstractPage {

    protected AbstractEndPage (Client client) {
        super(client);
    }

    public void onEnd () {
        client.setNextState(START_PAGE);
    }
}

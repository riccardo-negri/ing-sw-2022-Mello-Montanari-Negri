package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.BOARD_PAGE;

public abstract class AbstractMatchmakingPage extends AbstractPage {
    protected AbstractMatchmakingPage (Client client) {
        super(client);
    }

    public void onEnd () {
        client.setupConnection();
        client.setNextState(BOARD_PAGE);
    }
}

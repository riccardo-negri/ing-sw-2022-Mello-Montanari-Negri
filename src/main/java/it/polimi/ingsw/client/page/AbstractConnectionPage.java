package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.GAME_MODE_SELECTION_PAGE;

public abstract class AbstractConnectionPage extends AbstractPage {
    public AbstractConnectionPage (Client client) {
        super(client);
    }

    public void attemptConnection () {

    }

    public void onEnd (String IP, int port, String username) {
        client.setIpAddress(IP);
        client.setPort(port);
        client.setUsername(username);
        client.setNextState(GAME_MODE_SELECTION_PAGE);
    }
}

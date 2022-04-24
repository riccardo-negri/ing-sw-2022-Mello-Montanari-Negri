package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.Login;

import static it.polimi.ingsw.client.page.ClientState.GAME_MODE_SELECTION_PAGE;

public abstract class AbstractConnectionPage extends AbstractClientState{
    public AbstractConnectionPage (Client client) {
        super(client);
    }

    public void attemptConnection() {

    }

    public void onEnd(String IP, int port, String username) {
        client.setIPAddress(IP);
        client.setPort(port);
        client.setUsername(username);
        client.setNextState(GAME_MODE_SELECTION_PAGE);
    }
}

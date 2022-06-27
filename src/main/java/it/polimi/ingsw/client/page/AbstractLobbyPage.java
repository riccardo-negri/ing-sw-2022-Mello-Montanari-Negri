package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.networking.*;

import java.util.Arrays;
import java.util.logging.Level;

import static it.polimi.ingsw.client.page.ClientPage.BOARD_PAGE;

public abstract class AbstractLobbyPage extends AbstractPage {
    protected AbstractLobbyPage (Client client) {
        super(client);
    }

    /**
     * return when an initial state is received or a new user connected
     * @return username of connected user or null if a state has been received
     */
    public String waitForConnectedOrStart () {
        Message lastMessage = client.getConnection().waitMessage(Arrays.asList(InitialState.class, UserConnected.class));
        if (lastMessage instanceof UserConnected userConnected) {
            return userConnected.username();
        }
        else if (lastMessage instanceof InitialState initialState) {
            client.setModel(Game.request(Game.deserializeGameFromString(initialState.getState())));
            logger.log(Level.FINE, "Successfully loaded model sent by the server");
            client.setUsernames(initialState.getUsernames());
        }
        return null;
    }

    /**
     * set next state to BOARD_PAGE
     */
    public void onEnd () {
        client.setNextState(BOARD_PAGE);
    }
}

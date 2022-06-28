package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.networking.*;

import java.util.Arrays;

import static it.polimi.ingsw.client.page.ClientPage.*;

/**
 * abstract page for the CONNECTION state to be extended in the UI implementation
 */
public abstract class AbstractConnectionPage extends AbstractPage {
    protected AbstractConnectionPage (Client client) {
        super(client);
    }

    public void connectToMatchmakingServer (String ip, int port, String username) throws java.lang.RuntimeException {
        client.setIpAddress(ip);
        client.setPort(port);
        client.setUsername(username);

        Login login = new Login(client.getUsername());
        client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));
        client.getConnection().send(login);
    }

    /**
     * Wait for a message of type LobbiesList or Redirect and act subsequently
     * Returns false if the username is already taken, true otherwise
     */
    public boolean waitForLobbiesListOrRedirect () {
        Message lastMessage = client.getConnection().waitMessage(Arrays.asList(LobbiesList.class, Redirect.class, ErrorMessage.class));
        if (lastMessage instanceof ErrorMessage) {
            return false;
        }
        else if(lastMessage instanceof LobbiesList lobbiesList) { // we received a list of lobbies, so it's the first time the user logs in the server
            client.setLobbies(lobbiesList.getLobbies());
        }
        else if (lastMessage instanceof Redirect redirect) { // the user was already registered in the server, so he must be in a game
            client.setPort(redirect.port());
            client.getConnection().close();
            client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));
            Login login = new Login(client.getUsername());
            client.getConnection().send(login);
            client.setNextState(LOBBY_PAGE);
        }
        return true;
    }

    /**
     * set next state to CREATE_GAME_PAGE if createGame is true, otherwise LOBBY_SELECTION_PAGE
     * @param createGame true if the user decided to create a new game
     */
    public void onEnd(boolean createGame) {
        if (createGame) {
            client.setNextState(CREATE_GAME_PAGE);
        }
        else {
            client.setNextState(LOBBY_SELECTION_PAGE);
        }
    }
}

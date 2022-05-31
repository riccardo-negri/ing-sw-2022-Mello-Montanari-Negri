package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.networking.*;

import java.util.Arrays;

import static it.polimi.ingsw.client.page.ClientPage.LOBBY_PAGE;

public abstract class AbstractLobbySelectionPages extends AbstractPage {

    protected AbstractLobbySelectionPages (Client client) {
        super(client);
    }

    /**
     *
     * @param lobbyCode code of the lobby
     * @return true if joined successfully, false if the lobby was not existent of if it was full
     */
    public boolean tryToJoinLobby (String lobbyCode) {
        LobbyChoice choiceMessage = new LobbyChoice(lobbyCode);
        client.getConnection().send(choiceMessage);

        Message lastMessage = client.getConnection().waitMessage(Arrays.asList(Redirect.class, ErrorMessage.class));
        if (lastMessage instanceof Redirect redirect) {
            client.setPort(redirect.getPort());
            client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));

            Login login = new Login(client.getUsername());
            client.getConnection().send(login);
            return true;
        }
        else if (lastMessage instanceof  ErrorMessage) {
            return false; // redundant
        }
        return  false;
    }

    public void onEnd () {
        client.setNextState(LOBBY_PAGE);
    }
}

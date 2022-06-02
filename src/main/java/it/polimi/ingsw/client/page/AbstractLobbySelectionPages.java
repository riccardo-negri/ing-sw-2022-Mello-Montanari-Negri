package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.networking.*;

import java.util.Arrays;

import static it.polimi.ingsw.client.page.ClientPage.LOBBY_PAGE;

public abstract class AbstractLobbySelectionPages extends AbstractPage {

    protected AbstractLobbySelectionPages (Client client) {
        super(client);
    }

    private LobbyDescriptor getLobbyFromCode (String code) {
        for (LobbyDescriptor l : client.getLobbies()) {
            if (l.getCode().equals(code)) return l;
        }
        return null;
    }
    /**
     *
     * @param lobbyCode code of the lobby
     * @return true if joined successfully, false if the lobby was not existent of if it was full
     */
    public boolean tryToJoinLobby (String lobbyCode) {
        LobbyChoice choiceMessage = new LobbyChoice(lobbyCode);
        client.getConnection().send(choiceMessage);

        // if I receive LobbiesList it means that there was an error joining the selected lobby
        Message lastMessage = client.getConnection().waitMessage(Arrays.asList(Redirect.class, LobbiesList.class));
        if (lastMessage instanceof Redirect redirect) {
            client.setPort(redirect.port());
            client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));

            Login login = new Login(client.getUsername());
            client.getConnection().send(login);

            LobbyDescriptor currLobby = getLobbyFromCode(lobbyCode);
            if(currLobby != null) {
                client.setAdvancedGame(currLobby.getGameMode() == GameMode.COMPLETE);
                client.setPlayerNumber(currLobby.getPlayerNumber().getWizardNumber());
            }
            return true;
        }
        else if (lastMessage instanceof  LobbiesList lobbiesList) {
            client.setLobbies(lobbiesList.getLobbies());
            return false; // redundant
        }
        return  false;
    }

    public void onEnd () {
        client.setNextState(LOBBY_PAGE);
    }
}

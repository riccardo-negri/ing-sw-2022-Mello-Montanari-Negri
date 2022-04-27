package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.Login;

import static it.polimi.ingsw.client.page.ClientState.MATCHMAKING_PAGE;

public abstract class AbstractGameModeSelectionPage extends AbstractClientState {
    public AbstractGameModeSelectionPage (Client client) {
        super(client);
    }

    public void onEnd(int playerNumber, boolean isAdvancedGame) {
        client.setPlayerNumber(playerNumber);
        client.setAdvancedGame(isAdvancedGame);
        client.setupConnection();
        client.setNextState(MATCHMAKING_PAGE);
    }
}

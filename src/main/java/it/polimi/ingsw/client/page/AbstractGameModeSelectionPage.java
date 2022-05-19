package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;

import static it.polimi.ingsw.client.page.ClientPage.MATCHMAKING_PAGE;

public abstract class AbstractGameModeSelectionPage extends AbstractPage {
    protected AbstractGameModeSelectionPage (Client client) {
        super(client);
    }

    public void onEnd (int playerNumber, boolean isAdvancedGame) {
        client.setPlayerNumber(playerNumber);
        client.setAdvancedGame(isAdvancedGame);
        client.setNextState(MATCHMAKING_PAGE);
    }
}

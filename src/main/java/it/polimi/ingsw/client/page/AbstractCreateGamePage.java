package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.CreateLobby;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.Redirect;

import static it.polimi.ingsw.client.page.ClientPage.LOBBY_PAGE;

public abstract class AbstractCreateGamePage extends AbstractPage {
    protected AbstractCreateGamePage (Client client) {
        super(client);
    }

    public void createGameAndGoToLobby(int playerNumber, boolean isAdvancedGame) {
        client.setPlayerNumber(playerNumber);
        client.setAdvancedGame(isAdvancedGame);

        CreateLobby createMessage = new CreateLobby(PlayerNumber.fromNumber(client.getPlayerNumber()), client.isAdvancedGame() ? GameMode.COMPLETE : GameMode.EASY);
        client.getConnection().send(createMessage);

        Redirect redirect = (Redirect) client.getConnection().waitMessage(Redirect.class);
        client.setPort(redirect.port());
        client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));

        Login login = new Login(client.getUsername());
        client.getConnection().send(login);
    }

    public void onEnd () {
        client.setNextState(LOBBY_PAGE);
    }
}

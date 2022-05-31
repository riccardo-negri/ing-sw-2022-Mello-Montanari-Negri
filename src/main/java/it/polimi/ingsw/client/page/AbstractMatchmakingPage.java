package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.Redirect;

import java.util.logging.Level;

import static it.polimi.ingsw.client.page.ClientPage.BOARD_PAGE;
import static it.polimi.ingsw.client.page.ClientPage.MENU_PAGE;

public abstract class AbstractMatchmakingPage extends AbstractPage {
    protected AbstractMatchmakingPage (Client client) {
        super(client);
    }

    public void connectToMatchmakingServer () throws java.lang.RuntimeException {
        GameMode gm = client.isAdvancedGame() ? GameMode.COMPLETE : GameMode.EASY;
        Login login = new Login(client.getUsername(), PlayerNumber.fromNumber(client.getPlayerNumber()), gm);
        client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));
        client.getConnection().send(login);
    }

    public void connectToGameServer () throws java.lang.RuntimeException {
        Redirect redirect = (Redirect) client.getConnection().waitMessage(Redirect.class);
        client.setPort(redirect.getPort());
        client.setConnection(new Connection(client.getIpAddress(), client.getPort(), logger));
        GameMode gm = client.isAdvancedGame() ? GameMode.COMPLETE : GameMode.EASY;
        Login login = new Login(client.getUsername(), PlayerNumber.fromNumber(client.getPlayerNumber()), gm);
        client.getConnection().send(login);
    }

    public void waitForStartAndLoadInitialState () {
        InitialState initialState = (InitialState) client.getConnection().waitMessage(InitialState.class);
        try {
            client.setModel(Game.request(Game.deserializeGameFromString(initialState.getState())));
            logger.log(Level.FINE, "Successfully loaded model sent by the server");
        } catch (Exception e) {
            logger.log(Level.SEVERE, new StringBuilder().append("Couldn't load model sent by the server. Exception: ").append(e).toString());
        }
        client.setUsernames(initialState.getUsernames());
    }

    public void onEnd (boolean connected) {
        if (connected) {
            client.setNextState(BOARD_PAGE);
        }
        else {
            client.setNextState(MENU_PAGE);
        }
    }
}

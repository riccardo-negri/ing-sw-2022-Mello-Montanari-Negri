package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.Redirect;
import it.polimi.ingsw.networking.moves.CardChoice;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MovesTest {

    Logger logger = LogFormatter.getLogger("Test");

    @Test
    void startTest() throws Exception {
        Server s = new MatchmakingServer();
        new Thread(s::run).start();
        Login l1 = new Login("tommaso");
        Login l2 = new Login("riccardo");
        Connection c1 = enterGame(l1);
        Connection c2 = enterGame(l2);
        InitialState state1 = (InitialState) c1.waitMessage(InitialState.class);
        InitialState state2 = (InitialState) c2.waitMessage(InitialState.class);
        assert (state1.getState().equals(state2.getState()));
        logger.log(Level.INFO, "Initial state received correctly");
        /*int id = Game.deserializeGame(state1.getState());
        logger.log(Level.INFO, "Game loaded correctly");
        Game g = Game.request(id);*/
        //TODO: use deserialized game
        int id = Game.gameEntityFactory(GameMode.COMPLETE, PlayerNumber.TWO);
        Game g = Game.request(id);
        CardChoice client1Choice = new CardChoice(g.getWizard(0), 7);
        CardChoice client2Choice = new CardChoice(g.getWizard(1), 5);
        c1.send(client1Choice); // at least one of the two will work
        c2.send(client2Choice);
        CardChoice received1 = (CardChoice) c1.waitMessage(CardChoice.class);
        CardChoice received2 = (CardChoice) c2.waitMessage(CardChoice.class);
        assert (received1.getCard() == received2.getCard());
        String toLog = "The chosen card is " + received1.getCard();
        logger.log(Level.INFO, toLog);
        s.stop();
    }

    Connection enterGame(Login login) {
        Connection connection = new Connection("localhost", 50000, logger);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        String toLog = "port " + redirect.getPort();
        logger.log(Level.INFO, toLog);
        connection.close();
        connection = new Connection("localhost", redirect.getPort(), logger);
        connection.send(login);
        return connection;
    }
}

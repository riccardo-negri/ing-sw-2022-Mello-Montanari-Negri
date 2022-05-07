package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.InitialState;
import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Redirect;
import it.polimi.ingsw.utils.moves.CardChoice;
import org.junit.jupiter.api.Test;

public class MovesTest {
    @Test
    void startTest() throws Exception {
        Server s = new MatchmakingServer();
        new Thread(s::run).start();
        Login l1 = new Login("tommaso", PlayerNumber.TWO, GameMode.COMPLETE);
        Login l2 = new Login("riccardo", PlayerNumber.TWO, GameMode.COMPLETE);
        Connection c1 = enterGame(l1);
        Connection c2 = enterGame(l2);
        InitialState state1 = (InitialState) c1.waitMessage(InitialState.class);
        InitialState state2 = (InitialState) c2.waitMessage(InitialState.class);
        assert (state1.getState().equals(state2.getState()));
        System.out.println("Initial state received correctly");
        /*int id = Game.deserializeGame(state1.getState());
        System.out.println("Game loaded correctly");
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
        System.out.println("The chosen card is " + received1.getCard());
        s.stop();
    }

    Connection enterGame(Login login) {
        Connection connection = new Connection("localhost", 50000);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        System.out.println("port");
        System.out.println(redirect.getPort());
        connection.close();
        connection = new Connection("localhost", redirect.getPort());
        connection.send(login);
        return connection;
    }
}

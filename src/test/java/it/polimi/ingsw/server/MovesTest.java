package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.moves.CardChoice;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class MovesTest {

    Logger logger = LogFormatter.getLogger("Test");

    @BeforeEach
    void clearSaves() throws IOException {
        Procedures.clearFolder(logger);
    }

    @Test
    void startTest() {
        Server s = new MatchmakingServer();
        assert s.socket != null;
        new Thread(s::run).start();
        Connection c1 = Procedures.creatorLogin("tommaso", PlayerNumber.TWO, GameMode.COMPLETE, false, logger);
        Connection c2 = Procedures.joinerLogin("riccardo", false, logger);

        InitialState state1 = (InitialState) c1.waitMessage(InitialState.class);
        InitialState state2 = (InitialState) c2.waitMessage(InitialState.class);
        assert (state1.getState().equals(state2.getState()));
        logger.log(Level.INFO, "Initial state received correctly");
        int id = Game.deserializeGameFromString(state1.getState());
        Game g = Game.request(id);
        logger.log(Level.INFO, "Game loaded correctly");

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
}

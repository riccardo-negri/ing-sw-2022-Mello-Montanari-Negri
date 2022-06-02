package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;
import java.util.logging.Level;
import java.util.logging.Logger;

class ReconnectTest {
    Logger logger = LogFormatter.getLogger("Test");

    @Test
    void startTest() {
        Server s = new MatchmakingServer();
        new Thread(s::run).start();

        Connection c = Procedures.creatorLogin("tommaso", PlayerNumber.TWO, GameMode.COMPLETE, false, logger);
        Procedures.joinerLogin("riccardo", true, logger);
        c.close();
        Procedures.reconnectLogin("tommaso", true, logger);
        logger.log(Level.INFO, "Done");
        s.stop();
    }
}

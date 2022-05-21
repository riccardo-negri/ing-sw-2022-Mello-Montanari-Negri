package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.Redirect;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ReconnectTest {

    Logger logger = Logger.getLogger("MyLog");

    @Test
    void startTest() {
        Server s = new MatchmakingServer();
        new Thread(s::run).start();
        Login l1 = new Login("tommaso", PlayerNumber.TWO, GameMode.COMPLETE);
        Login l2 = new Login("riccardo", PlayerNumber.TWO, GameMode.COMPLETE);
        Connection c = enterGame(l1, false);
        enterGame(l2, true);
        c.close();
        enterGame(l1, true);
        logger.log(Level.INFO, "Done");
        s.stop();
    }

    Connection enterGame(Login login, boolean wait) {
        Connection connection = new Connection("localhost", 50000, logger);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        String toLog = "port " + redirect.getPort();
        logger.log(Level.INFO, toLog);
        connection.close();
        connection = new Connection("localhost", redirect.getPort(), logger);
        connection.send(login);
        if (wait) {
            connection.waitMessage(InitialState.class);
            connection.close();
        }
        return connection;
    }
}

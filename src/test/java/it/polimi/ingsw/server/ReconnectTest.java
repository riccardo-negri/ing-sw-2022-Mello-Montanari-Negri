package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.Login;
import it.polimi.ingsw.networking.Redirect;
import org.junit.jupiter.api.Test;

public class ReconnectTest {
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
        System.out.println("done");
        s.stop();
    }

    Connection enterGame(Login login, boolean wait) {
        Connection connection = new Connection("localhost", 50000);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        System.out.println("port");
        System.out.println(redirect.getPort());
        connection.close();
        connection = new Connection("localhost", redirect.getPort());
        connection.send(login);
        if (wait) {
            connection.waitMessage(InitialState.class);
            connection.close();
        }
        return connection;
    }
}

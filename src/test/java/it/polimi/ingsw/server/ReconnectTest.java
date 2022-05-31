package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReconnectTest {

    Logger logger = LogFormatter.getLogger("Test");

    @Test
    void startTest() {
        Server s = new MatchmakingServer();
        new Thread(s::run).start();
        Login l1 = new Login("tommaso");
        Login l2 = new Login("riccardo");

        Result r1 = enterMatchmaking(l1);
        r1.connection.send(new CreateLobby(PlayerNumber.TWO, GameMode.COMPLETE));
        Redirect redirect = (Redirect) r1.connection.waitMessage(Redirect.class);
        Connection c = enterGame(r1.connection, redirect, l1, false);

        Result r2 = enterMatchmaking(l2);
        r2.connection.send(new LobbyChoice(((LobbiesList) r2.message).getLobbies().get(0).getCode()));
        redirect = (Redirect) r2.connection.waitMessage(Redirect.class);
        enterGame(r2.connection, redirect, l2, true);
        c.close();
        r1 = enterMatchmaking(l1);
        enterGame(r1.connection, (Redirect) r1.message, l1, true);
        System.out.println(r1.message);
        logger.log(Level.INFO, "Done");
        s.stop();
    }

    Result enterMatchmaking(Login login) {
        Connection connection = new Connection("localhost", 50000, logger);
        connection.send(login);
        Message message = connection.waitMessage(Arrays.asList(LobbiesList.class, Redirect.class));
        return new Result(connection, message);
    }

    Connection enterGame(Connection connection, Redirect redirect, Login login, boolean wait) {
        String toLog = "port " + redirect.getPort();
        logger.log(Level.INFO, toLog);
        connection.close();
        connection = new Connection("localhost", redirect.getPort(), logger);
        connection.send(login);
        if (wait) {
            Message m;
            do {
                m = connection.waitMessage(Arrays.asList(InitialState.class, UserConnected.class));
            } while (m instanceof  UserConnected);
            connection.close();
        }
        return connection;
    }
}


class Result {
    Connection connection;
    Message message;

    public Result(Connection connection, Message message) {
        this.connection = connection;
        this.message = message;
    }
}

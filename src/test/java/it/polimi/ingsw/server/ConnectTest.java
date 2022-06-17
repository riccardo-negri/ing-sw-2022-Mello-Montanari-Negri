package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class ConnectTest {
    Logger logger = LogFormatter.getLogger("Test");

    @Test
    void reconnectTest() throws InterruptedException {
        Server s = new MatchmakingServer();
        Thread t = new Thread(s::run);
        t.start();

        /* there are three user connecting to the server
        * one creates a game and tries to reconnect to it afterwards
        * one joins the game
        * and one waits without sending the login information and is disconnected when the server stops
        * */

        Connection creator = Procedures.creatorLogin("tommaso", PlayerNumber.TWO, GameMode.COMPLETE, false, logger);
        Connection joiner = Procedures.joinerLogin("riccardo", true, logger);
        Connection connecting = new Connection("localhost", MatchmakingServer.WELL_KNOWN_PORT, logger);
        creator.close();
        UserDisconnected ud = (UserDisconnected) joiner.waitMessage(UserDisconnected.class);
        assert ud.username().equals("tommaso");  // tommaso just left
        joiner.close();
        joiner = Procedures.reconnectLogin("riccardo", true, logger);
        ud = (UserDisconnected) joiner.waitMessage(UserDisconnected.class);
        assert ud.username().equals("tommaso");  // when riccardo enters is notified that tommaso is missing
        creator = Procedures.reconnectLogin("tommaso", true, logger);
        logger.log(Level.INFO, "Done");
        s.stop();
        t.join();
        joiner.waitMessage(Disconnected.class);
        creator.waitMessage(Disconnected.class);
        connecting.waitMessage(Disconnected.class);
    }

    @Test
    void connectionRefusedTest() throws InterruptedException {
        Server s = new MatchmakingServer();
        Thread t = new Thread(s::run);
        t.start();

        Connection creator = Procedures.creatorLogin("tommaso", PlayerNumber.THREE, GameMode.COMPLETE, false, logger);
        int remotePort = creator.getSocket().getRemotePort();
        Connection homonym = Procedures.reconnectLogin("tommaso", false, logger);
        homonym.waitMessage(Disconnected.class);
        homonym.close();
        Connection hacker = new Connection("localhost", remotePort, logger);
        hacker.send(new Login("riccardo"));
        hacker.waitMessage(Disconnected.class);
        hacker.close();
        hacker = new Connection("localhost", remotePort, logger); // tries to connect directly to game server
        hacker.send(new Login("tommaso"));
        hacker.waitMessage(Disconnected.class);
        hacker.close();
        Connection brokenClient = new Connection("localhost", remotePort, logger);
        brokenClient.send(new LobbyChoice("#f2o"));
        brokenClient.waitMessage(Disconnected.class);
        brokenClient.close();
        s.stop();
        t.join();
        creator.waitMessage(Disconnected.class);
    }

    @Test
    void resignTest() throws InterruptedException {
        MatchmakingServer s = new MatchmakingServer();
        Thread t = new Thread(s::run);
        t.start();

        Connection creator = Procedures.creatorLogin("tommaso", PlayerNumber.TWO, GameMode.COMPLETE, false, logger);
        Connection joiner = Procedures.joinerLogin("riccardo", true, logger);
        joiner.send(new UserResigned("riccardo"));
        UserResigned ur = (UserResigned) creator.waitMessage(UserResigned.class);
        assert ur.getUsername().equals("riccardo");

        TimeUnit.MILLISECONDS.sleep(100);
        assert s.noGamesRunning();
        s.stop();
        t.join();
    }


}

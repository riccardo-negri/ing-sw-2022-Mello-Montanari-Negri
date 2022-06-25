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
        assert s.socket != null;
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
    void lobbyReconnectTest() throws InterruptedException {
        Server s = new MatchmakingServer();
        assert s.socket != null;
        Thread t = new Thread(s::run);
        t.start();

        /* four are three user connecting to the server
         * one creates a game and tries to reconnect when the game is started
         * one joins the game and tries to reconnect while still in the lobby
         * one joins the game and waits in the lobby
         * one joins the game at the end and makes it start
         * */

        Connection creator = Procedures.creatorLogin("tommaso", PlayerNumber.FOUR, GameMode.COMPLETE, false, logger);
        creator.waitMessage(UserConnected.class);
        assert creator.noMessageLeft();
        Connection reconnectGuy = Procedures.joinerLogin("riccardo", false, logger);
        creator.waitMessage(UserConnected.class);
        reconnectGuy.waitMessage(UserConnected.class);
        reconnectGuy.waitMessage(UserConnected.class);
        assert creator.noMessageLeft();
        assert reconnectGuy.noMessageLeft();
        creator.close();
        reconnectGuy.close();

        Connection joiner = Procedures.joinerLogin("pietro", false, logger);
        joiner.waitMessage(UserConnected.class);
        joiner.waitMessage(UserConnected.class);
        joiner.waitMessage(UserConnected.class);
        assert joiner.noMessageLeft();

        reconnectGuy = Procedures.reconnectLogin("riccardo", false, logger);
        reconnectGuy.waitMessage(UserConnected.class);
        reconnectGuy.waitMessage(UserConnected.class);
        reconnectGuy.waitMessage(UserConnected.class);
        assert reconnectGuy.noMessageLeft();
        assert joiner.noMessageLeft();
        assert creator.noMessageLeft();

        Connection lastGuy = Procedures.joinerLogin("davide", false, logger);
        lastGuy.waitMessage(UserConnected.class);
        lastGuy.waitMessage(UserConnected.class);
        lastGuy.waitMessage(UserConnected.class);
        lastGuy.waitMessage(UserConnected.class);
        reconnectGuy.waitMessage(UserConnected.class);
        joiner.waitMessage(UserConnected.class);

        lastGuy.waitMessage(InitialState.class);
        reconnectGuy.waitMessage(InitialState.class);
        joiner.waitMessage(InitialState.class);

        lastGuy.waitMessage(UserDisconnected.class);
        reconnectGuy.waitMessage(UserDisconnected.class);
        joiner.waitMessage(UserDisconnected.class);

        assert reconnectGuy.noMessageLeft();
        assert joiner.noMessageLeft();
        assert lastGuy.noMessageLeft();

        creator = Procedures.reconnectLogin("tommaso", false, logger);
        creator.waitMessage(InitialState.class);
        creator.waitMessage(UserConnected.class);
        joiner.waitMessage(UserConnected.class);
        reconnectGuy.waitMessage(UserConnected.class);
        lastGuy.waitMessage(UserConnected.class);

        assert reconnectGuy.noMessageLeft();
        assert joiner.noMessageLeft();
        assert creator.noMessageLeft();
        assert lastGuy.noMessageLeft();

        s.stop();
        t.join();
    }

    @Test
    void connectionRefusedTest() throws InterruptedException {
        Server s = new MatchmakingServer();
        assert s.socket != null;
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
        assert s.socket != null;
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

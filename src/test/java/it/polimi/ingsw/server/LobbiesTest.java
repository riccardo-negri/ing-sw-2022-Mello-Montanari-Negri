package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class LobbiesTest {

    Logger logger = LogFormatter.getLogger("Test");

    @Test
    void wrongCodeTest() throws InterruptedException {
        Server s = new MatchmakingServer();
        assert s.socket != null;
        Thread t = new Thread(s::run);
        t.start();

        Connection creator = Procedures.creatorLogin("tommaso", PlayerNumber.TWO, GameMode.COMPLETE, false, logger);

        Connection alwaysRefused = new Connection("localhost", MatchmakingServer.WELL_KNOWN_PORT, logger);
        alwaysRefused.send( new Login("riccardo"));
        alwaysRefused.waitMessage(LobbiesList.class);
        alwaysRefused.send(new LobbyChoice("0000")); // wrong code
        LobbiesList list = (LobbiesList) alwaysRefused.waitMessage(LobbiesList.class);

        Connection joiner = Procedures.joinerLogin("pietro", true, logger);


        alwaysRefused.send(new LobbyChoice(list.getLobbies().get(0).getCode())); // lobby is full
        list = (LobbiesList) alwaysRefused.waitMessage(LobbiesList.class);
        assert list.getLobbies().size() == 0;
        s.stop();
        t.join();
        creator.waitMessage(Disconnected.class);
        joiner.waitMessage(Disconnected.class);
        alwaysRefused.waitMessage(Disconnected.class);
    }

    @Test
    void badLobbyCreationFormatTest() throws InterruptedException {

        Server s = new MatchmakingServer();
        assert s.socket != null;
        Thread t = new Thread(s::run);
        t.start();

        Connection creator = new Connection("localhost", MatchmakingServer.WELL_KNOWN_PORT, logger);
        creator.send(new Login("tommaso"));
        creator.waitMessage(LobbiesList.class);
        creator.send(new CreateLobby(null, GameMode.COMPLETE));
        creator.send(new CreateLobby(PlayerNumber.TWO, null));
        TimeUnit.MILLISECONDS.sleep(100);
        assert creator.noMessageLeft();  // no response because previous messages were refused
        creator.send(new CreateLobby(PlayerNumber.TWO, GameMode.COMPLETE));
        creator.waitMessage(Redirect.class);

        s.stop();
        t.join();
        creator.waitMessage(Disconnected.class);
    }
}

package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class NetworkingTest {

    Logger logger = LogFormatter.getLogger("Test");
    private static final List<String> usernames1 = Arrays.asList("user1", "user2", "user3", "user4");
    private static final List<String> usernames2 = Arrays.asList("user1", "user2", "user3", "u");

    @Test
    void  lobbyDescriptorTest() {
        LobbyDescriptor ld1 = new LobbyDescriptor("#jn3", PlayerNumber.TWO, GameMode.COMPLETE, usernames1);
        LobbyDescriptor ld2 = new LobbyDescriptor("#Jn3", PlayerNumber.TWO, GameMode.COMPLETE, usernames1);
        LobbyDescriptor ld3 = new LobbyDescriptor("#jn3", PlayerNumber.FOUR, GameMode.COMPLETE, usernames1);
        LobbyDescriptor ld4 = new LobbyDescriptor("#jn3", PlayerNumber.TWO, GameMode.EASY, usernames1);
        LobbyDescriptor ld5 = new LobbyDescriptor("#jn3", PlayerNumber.TWO, GameMode.COMPLETE, usernames2);

        assert !ld1.equals(ld2);
        assert !ld1.equals(ld3);
        assert !ld1.equals(ld4);
        assert !ld1.equals(ld5);
    }

    @Test
    void bindFunctionTest() {
        List<Connection> connections = Procedures.twoBoundedConnections(logger);
        connections.get(0).send(new Redirect(30000));
        connections.get(0).send(new ErrorMessage());
        connections.get(0).send(new Redirect(30000));
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        connections.get(1).bindFunctionAndTestPrevious(this::acceptRedirect);
        connections.get(1).waitMessage(ErrorMessage.class);
        assert connections.get(1).noMessageLeft();
        connections.get(0).close();
        connections.get(1).close();
    }

    boolean acceptRedirect(Connection c) {
        if (c.getLastMessage() instanceof Redirect r) {
            assert r.port() == 30000;
            return true;
        }
        return false;
    }

    @Test
    void createWithPredicateTest() {
        List<Connection> connections = Procedures.twoBoundedConnectionsWithFunction(logger, this::acceptRedirect);
        connections.get(0).send(new Redirect(30000));
        connections.get(0).close();
        connections.get(1).close();
    }
}

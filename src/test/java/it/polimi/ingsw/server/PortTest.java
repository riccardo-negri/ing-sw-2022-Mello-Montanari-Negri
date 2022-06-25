package it.polimi.ingsw.server;

import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class PortTest{
    Logger logger = LogFormatter.getLogger("Test");

    Server s2;

    @Test
    void portNotAvailableTest() throws InterruptedException {
        Server s1 = new MatchmakingServer();
        assert s1.socket != null;
        Thread t1 = new Thread(s1::run);
        t1.start();

        Thread t2 = new Thread(() -> {
            s2 = new MatchmakingServer();
        });
        t2.start();
        TimeUnit.MILLISECONDS.sleep(500);
        s1.stop();  // now I close the first server and the second is free to connect to the port
        t1.join();
        t2.join();

        Thread t3 = new Thread(s2::run);
        t3.start();
        new Connection("localhost", MatchmakingServer.WELL_KNOWN_PORT, logger);
        TimeUnit.MILLISECONDS.sleep(100); // wait for server to update connecting list
        assert (1== s2.connecting.size());
        s2.stop();
        t3.join();
    }
}

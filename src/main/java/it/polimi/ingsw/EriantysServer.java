package it.polimi.ingsw;

import it.polimi.ingsw.server.MatchmakingServer;
import it.polimi.ingsw.utils.LogFormatter;

import java.util.Arrays;
import java.util.List;

/**
 * entry point opf the server
 */
public class EriantysServer {
    /**
     * run matchmaking server, if a specific argument is present activate debug mode
     * @param args program arguments set on launch (not used)
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.contains("-d") || argsList.contains("--debug")) {
            LogFormatter.setDebugActivated(true);
        }
        new MatchmakingServer().run();
    }
}

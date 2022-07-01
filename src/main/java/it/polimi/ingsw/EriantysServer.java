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
     * @param args program arguments set on launch
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        if (argsList.contains("-d") || argsList.contains("--debug")) {
            LogFormatter.setDebugActivated(true);
        }
        if (argsList.contains("-p")) {
            setMatchmakingPort(argsList, argsList.indexOf("-p")+1);
        }
        if (argsList.contains("--port")) {
            setMatchmakingPort(argsList, argsList.indexOf("--port")+1);
        }
        new MatchmakingServer().run();
    }

    /**
     * sets a custom port for the matchmaking server
     * @param argsList program arguments set on launch
     * @param portIndex the index of the port number string in the arguments list
     */
    private static void setMatchmakingPort(List<String> argsList, int portIndex) {
        if (argsList.size() > portIndex){
            int port;
            try {
                port = Integer.parseInt(argsList.get(portIndex));
                if (port >= 49152 && port <= 65535) {
                    MatchmakingServer.wellKnownPort = port;
                }
            } catch (NumberFormatException ignored) {/*ignored*/}
        }
    }
}

package it.polimi.ingsw;

import it.polimi.ingsw.server.MatchmakingServer;

public class EriantysServer {
    /**
     * run matchmaking server
     * @param args program arguments set on launch (not used)
     */
    public static void main(String[] args) {
        new MatchmakingServer().run();
    }
}

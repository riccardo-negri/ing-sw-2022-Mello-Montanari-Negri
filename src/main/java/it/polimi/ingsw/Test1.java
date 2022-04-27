package it.polimi.ingsw;

import it.polimi.ingsw.server.MatchmakingServer;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.moves.InitialState;
import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Redirect;

public class Test1 {
    public static void main(String[] args) {
        Server s = new MatchmakingServer();
        new Thread(s::run).start();
        Login l1 = new Login("tommaso", 2, true);
        Login l2 = new Login("riccardo", 2, true);
        Connection c = enterGame(l1, false);
        enterGame(l2, true);
        c.close();
        enterGame(l1, true);
        System.out.println("fine");
    }

    static Connection enterGame(Login login, boolean wait) {
        Connection connection = new Connection("localhost", 50000);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        System.out.println("porta");
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

package it.polimi.ingsw.server;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class Procedures {

    static void clearFolder(Logger logger) throws IOException {
        File f = new File(SavesManager.SAVES_ROOT);
        FileUtils.deleteDirectory(f);
        new MainSavesManager(logger).createSavesFolder();
    }

    static Connection creatorLogin(String username, PlayerNumber number, GameMode mode, boolean wait, Logger logger) {
        Login login = new Login(username);
        Connection connection = new Connection("localhost", 50000, logger);
        connection.send(login);
        connection.waitMessage(LobbiesList.class);
        connection.send(new CreateLobby(number, mode));
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        connection.close();
        connection = new Connection("localhost", redirect.port(), logger);
        connection.send(login);
        if (wait) {
            Message m = connection.waitMessage(InitialState.class);
        }
        return connection;
    }

    static Connection joinerLogin(String username, boolean wait, Logger logger) {
        Login login = new Login(username);
        Connection connection = new Connection("localhost", 50000, logger);
        connection.send(login);
        LobbiesList list = (LobbiesList) connection.waitMessage(LobbiesList.class);
        connection.send(new LobbyChoice(list.getLobbies().get(0).getCode()));
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        connection.close();
        connection = new Connection("localhost", redirect.port(), logger);
        connection.send(login);
        if (wait) {
            Message m = connection.waitMessage(InitialState.class);
        }
        return connection;
    }

    static Connection reconnectLogin(String username, boolean wait, Logger logger) {
        return reconnectLogin(username, wait, logger, MatchmakingServer.WELL_KNOWN_PORT);
    }

    static Connection reconnectLogin(String username, boolean wait, Logger logger, int port) {
        Login login = new Login(username);
        Connection connection = new Connection("localhost", port, logger);
        connection.send(login);
        Message message = connection.waitMessage(Arrays.asList(Redirect.class, ErrorMessage.class));
        if (message instanceof Redirect redirect) {
            connection.close();
            connection = new Connection("localhost", redirect.port(), logger);
            connection.send(login);
            if (wait) {
                Message m = connection.waitMessage(InitialState.class);
            }
        }
        return connection;
    }
}

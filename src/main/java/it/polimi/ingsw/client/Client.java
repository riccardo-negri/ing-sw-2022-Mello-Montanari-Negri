package it.polimi.ingsw.client;

import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.utils.LogFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private final UI ui;
    private ClientPage nextState;
    private String ipAddress;
    private int port;
    private String username;
    private int playerNumber;
    private boolean isAdvancedGame;
    private Connection connection;
    private final Logger logger;
    private List<String> usernames;
    private final ArrayList<String> usernamesDisconnected = new ArrayList<>();
    private Game model;

    public Client (boolean hasGUI) {
        if (hasGUI) {
            ui = null;
        }
        else {
            ui = new CLI();
            ((CLI) ui).init();
        }
        nextState = ClientPage.WELCOME_PAGE;

        logger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            logger.setUseParentHandlers(false);
            fh = new FileHandler("./log.txt");
            logger.setLevel(Level.ALL);
            logger.addHandler(fh);
            LogFormatter formatter = new LogFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void start () {
        while (nextState != null) {
            AbstractPage currState = ui.getState(this, nextState);
            currState.draw(this);   // draw does everything
        }
    }

    public ClientPage getNextState () {
        return nextState;
    }

    public void setNextState (ClientPage nextState) {
        this.nextState = nextState;
    }

    public void setupConnection () {
        GameMode gm = isAdvancedGame ? GameMode.COMPLETE : GameMode.EASY;
        Login login = new Login(username, PlayerNumber.fromNumber(playerNumber), gm);
        connection = new Connection(ipAddress, port);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        port = redirect.getPort();
        connection = new Connection(ipAddress, port);
        connection.send(login);

        // import initial state
        InitialState initialState = (InitialState) connection.waitMessage(InitialState.class);
        try {
            model = Game.request(Game.deserializeGameFromString(initialState.getState()));
            logger.log(Level.FINE, "Successfully loaded model sent by the server");
        } catch (Exception e) {
            logger.log(Level.SEVERE, new StringBuilder().append("Couldn't load model sent by the server. Exception: ").append(e).toString());
        }
        usernames = initialState.getUsernames();
    }

    public UI getUI () {
        return ui;
    }

    public String getIpAddress () {
        return ipAddress;
    }

    public void setIpAddress (String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort () {
        return port;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public int getPlayerNumber () {
        return playerNumber;
    }

    public void setPlayerNumber (int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void setAdvancedGame (boolean advancedGame) {
        isAdvancedGame = advancedGame;
    }

    public Connection getConnection () {
        return connection;
    }

    public void setConnection (Connection connection) {
        this.connection = connection;
    }

    public void setPort (int port) {
        this.port = port;
    }

    public List<String> getUsernames () {
        return usernames;
    }

    public Logger getLogger () {
        return logger;
    }

    public Game getModel () {
        return model;
    }

    public List<String> getUsernamesDisconnected () {
        return usernamesDisconnected;
    }

}

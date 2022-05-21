package it.polimi.ingsw.client;

import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.networking.*;
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
    private ArrayList<String> usernames;
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

        logger = LogFormatter.getLogger("Client");
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

    public boolean isAdvancedGame () {
        return isAdvancedGame;
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

    public void setUsernames (List<String> usernames) {
        this.usernames = (ArrayList<String>) usernames;
    }

    public Logger getLogger () {
        return logger;
    }

    public Game getModel () {
        return model;
    }

    public void setModel (Game model) {
        this.model = model;
    }

    public List<String> getUsernamesDisconnected () {
        return usernamesDisconnected;
    }

}

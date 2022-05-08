package it.polimi.ingsw.client;

import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.client.ui.cli.WelcomePageCLI;
import it.polimi.ingsw.model.entity.JsonDeserializerClass;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.utils.InitialState;

import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    private final UI ui;
    private ClientState nextState;
    private boolean newState;
    private WelcomePageCLI temp;
    private AbstractClientState currState;
    public String IPAddress;
    public int port;
    public String username;
    public int playerNumber;
    public boolean isAdvancedGame;
    public Connection connection;
    public Login login;

    public ArrayList<String> getUsernames () {
        return usernames;
    }

    public ArrayList<String> usernames;

    public Game getModel () {
        return model;
    }

    public Game model;

    public Client (boolean hasGUI) {
        if (hasGUI) {
            ui = null;
        }
        else {
            ui = new CLI();
            ((CLI) ui).init();
        }
        nextState = ClientState.BOARD_PAGE;
        newState = true;

        //TODO connect to server
        try{ model = Game.request(Game.deserializeGame("./../src/main/java/it/polimi/ingsw/client/serialized_states/s1.json"));
        } catch (Exception e) { System.out.println(e.toString()); }
        usernames = new ArrayList<>(Arrays.asList("Ric", "Tom", "Pietro", "Sanp"));
        IPAddress = "testnet";
        port = 1000;
    }

    public void start () {
        while (nextState != null) {
            currState = ui.getState(this, nextState);
            currState.draw(this);
        }
    }

    public ClientState getNextState () {
        return nextState;
    }

    public void setNextState (ClientState nextState) {
        this.nextState = nextState;
    }

    public void setupConnection(){
        GameMode gm = isAdvancedGame ? GameMode.COMPLETE : GameMode.EASY;
        login = new Login(username, PlayerNumber.fromNumber(playerNumber), gm);
        Connection connection = new Connection(IPAddress, port);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        System.out.println("porta");
        System.out.println(redirect.getPort());
        connection = new Connection(IPAddress, redirect.getPort());
        connection.send(login);
        connection.waitMessage(InitialState.class);

        // waiting for players
    }

    public UI getUI () {
        return ui;
    }

    public String getIPAddress () {
        return IPAddress;
    }

    public void setIPAddress (String IPAddress) {
        this.IPAddress = IPAddress;
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
}

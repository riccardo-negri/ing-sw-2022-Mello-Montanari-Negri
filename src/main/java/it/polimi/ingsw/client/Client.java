package it.polimi.ingsw.client;

import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.client.ui.cli.WelcomePageCLI;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.utils.InitialState;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    public Logger getLogger () {
        return LOGGER;
    }

    private final Logger LOGGER;

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
        nextState = ClientState.WELCOME_PAGE;
        newState = true;

        LOGGER = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            LOGGER.setUseParentHandlers(false);
            fh = new FileHandler("./log.txt");
            LOGGER.setLevel(Level.ALL);
            LOGGER.addHandler(fh);
            LogFormatter formatter = new LogFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        //TODO cancel
        //try{ model = Game.request(Game.deserializeGame("./../src/main/java/it/polimi/ingsw/client/serialized_states/s1.json"));
        //} catch (Exception e) { System.out.println(e.toString()); }
        //usernames = new ArrayList<>(Arrays.asList("Ric", "Tom", "Pietro", "Sanp"));
        //IPAddress = "testnet";
        //port = 1000;
    }

    public void start () {
        while (nextState != null) {
            currState = ui.getState(this, nextState);
            currState.draw(this);   // draw does everything
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
        connection = new Connection(IPAddress, port);
        connection.send(login);
        Redirect redirect = (Redirect) connection.waitMessage(Redirect.class);
        System.out.println("porta");
        System.out.println(redirect.getPort());
        port = redirect.getPort();
        connection = new Connection(IPAddress, port);
        connection.send(login);

        // import initial state
        InitialState initialState = (InitialState) connection.waitMessage(InitialState.class);
        try {
            FileWriter myWriter = new FileWriter("test.txt");
            myWriter.write(initialState.getState());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try{
            model = Game.request(Game.deserializeGameFromString(initialState.getState()));
            LOGGER.log(Level.FINE, "Successfully loaded model sent by the server");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Couldn't load model sent by the server. Exception: " + e.toString());
        }
        usernames = initialState.getUsernames();
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

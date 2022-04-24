package it.polimi.ingsw.client;

import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.CoreCLI;
import it.polimi.ingsw.client.ui.cli.WelcomePageCLI;
import it.polimi.ingsw.utils.*;

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
    public Boolean SetupConnectionLock = false;

    public Client(boolean hasGUI) {
        if(hasGUI) {
            ui = null;
        }
        else {
            ui = new CoreCLI();
        }
        nextState = ClientState.WELCOME_PAGE;
        newState = true;
    }

    public void start() {
        while(nextState != null) {
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
        Connection connection = new Connection(IPAddress, port, this::onRedirect);
        login = new Login(username, playerNumber, isAdvancedGame);
        connection.send(login);
    }

    private void onRedirect(ReceivedMessage message) {
        synchronized (SetupConnectionLock) {
            Redirect redirect = (Redirect) message.getContent();
            System.out.println("porta");
            System.out.println(redirect.getPort());
            connection = new ReplacingConnection(IPAddress, redirect.getPort(), this::onMessage, connection);
            connection.send(login);
            SetupConnectionLock = true;
            SetupConnectionLock.notifyAll();
        }

    }

    void onMessage(ReceivedMessage message) {

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

    public boolean getConnectionLock() {
        return SetupConnectionLock;
    }

    public void setPort (int port) {
        this.port = port;
    }
}

package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.networking.Connection;

/**
 * the special kind of user used from the game server, it is connected to the corresponding wizard object in the model
 */
public class GameUser extends User {

    private final Wizard wizard;
    private boolean isDisconnected = false;

    /**
     * associates a wizard to a username and a connection
     * @param name the username of this user
     * @param connection the connection to the user client
     * @param wizard the wizard controlled by the user
     */
    public GameUser(String name, Connection connection, Wizard wizard) {
        super(name, connection);
        this.wizard = wizard;
    }

    /**
     * get wizard value
     * @return the wizard controlled by the user
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     * detects connection problem of the user
     * @return if the user is disconnected from the server
     */
    public boolean isDisconnected() {
        return isDisconnected;
    }

    /**
     * mark this user as disconnected
     * @param isDisconnected if the user is disconnected from the server
     */
    public void setDisconnected(boolean isDisconnected) {
        this.isDisconnected = isDisconnected;
    }
}

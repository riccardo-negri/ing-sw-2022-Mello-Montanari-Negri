package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.networking.Connection;

public class GameUser extends User {

    private final Wizard wizard;
    private boolean isDisconnected = false;

    public GameUser(String name, Connection connection, Wizard wizard) {
        super(name, connection);
        this.wizard = wizard;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    public void setDisconnected(boolean isDisconnected) {
        this.isDisconnected = isDisconnected;
    }
}

package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.utils.Connection;

public class GameUser extends User {

    private final Wizard wizard;

    public GameUser(String name, Connection connection, Wizard wizard) {
        super(name, connection);
        this.wizard = wizard;
    }

    public Wizard getWizard() {
        return wizard;
    }
}

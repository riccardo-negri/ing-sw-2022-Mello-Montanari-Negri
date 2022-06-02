package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import it.polimi.ingsw.client.ui.gui.javaFX.JavaFXWelcomePage;
import javafx.stage.Stage;

import java.util.logging.Level;

public class WelcomePageGUI extends AbstractWelcomePage{

    protected WelcomePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {

    }
}

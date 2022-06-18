package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import it.polimi.ingsw.client.ui.gui.controllers.MenuPageController;
import it.polimi.ingsw.client.ui.gui.controllers.WelcomePageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WelcomePageGUI extends AbstractWelcomePage{

    protected WelcomePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Welcome!", "/fxml/WelcomePage.fxml");
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import it.polimi.ingsw.client.ui.gui.controllers.WelcomePageController;

public class WelcomePageGUI extends AbstractWelcomePage{

    protected WelcomePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Welcome!", "/fxml/WelcomePage.fxml", new WelcomePageController());
    }
}

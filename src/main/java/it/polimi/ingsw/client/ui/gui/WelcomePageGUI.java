package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import it.polimi.ingsw.client.ui.gui.controllers.WelcomePageController;

public class WelcomePageGUI extends AbstractWelcomePage{

    /**
     * pass client to AbstractWelcomePage constructor
     * @param client the client that is showing this page
     */
    protected WelcomePageGUI(Client client) {
        super(client);
    }

    /**
     * load the fxml file and give the correct controller and title to it
     */
    @Override
    public void draw() {
        showGUIPage("Welcome!", "/fxml/WelcomePage.fxml", new WelcomePageController());
    }
}

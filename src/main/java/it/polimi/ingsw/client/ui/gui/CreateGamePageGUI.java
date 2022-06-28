package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;
import it.polimi.ingsw.client.ui.gui.controllers.CreateGamePageController;

/**
 * GUI implementation of the CreateGame
 */
public class CreateGamePageGUI extends AbstractCreateGamePage {

    /**
     * pass client to AbstractCreateGamePage constructor
     * @param client the client that is showing this page
     */
    protected CreateGamePageGUI(Client client) {
        super(client);
    }

    /**
     * load the fxml file and give the correct controller and title to it
     */
    @Override
    public void draw() {
        showGUIPage("Create Game", "/fxml/CreateGamePage.fxml", new CreateGamePageController());
    }
}

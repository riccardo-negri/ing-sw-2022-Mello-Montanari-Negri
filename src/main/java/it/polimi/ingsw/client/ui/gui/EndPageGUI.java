package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractEndPage;
import it.polimi.ingsw.client.ui.gui.controllers.EndPageController;

public class EndPageGUI extends AbstractEndPage {

    /**
     * pass client to AbstractEndPage constructor
     * @param client the client that is showing this page
     */
    protected EndPageGUI(Client client) {
        super(client);
    }

    /**
     * load the fxml file and give the correct controller and title to it
     */
    @Override
    public void draw() {
        showGUIPage("Game Ended", "/fxml/EndPage.fxml", new EndPageController());
    }
}

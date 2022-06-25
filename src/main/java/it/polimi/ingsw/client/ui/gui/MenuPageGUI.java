package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMenuPage;
import it.polimi.ingsw.client.ui.gui.controllers.MenuPageController;

public class MenuPageGUI extends AbstractMenuPage {

    /**
     * pass client to AbstractMenuPage constructor
     * @param client the client that is showing this page
     */
    protected MenuPageGUI(Client client) {
        super(client);
    }

    /**
     * load the fxml file and give the correct controller and title to it
     */
    @Override
    public void draw() {
        showGUIPage("Menu", "/fxml/MenuPage.fxml", new MenuPageController());
    }
}

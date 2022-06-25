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

    @Override
    public void draw() {
        showGUIPage("Menu", "/fxml/MenuPage.fxml", new MenuPageController());
    }
}

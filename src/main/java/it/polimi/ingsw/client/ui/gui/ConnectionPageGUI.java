package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.ui.gui.controllers.ConnectionPageController;

public class ConnectionPageGUI extends AbstractConnectionPage {

    /**
     * pass client to AbstractConnectionPage constructor
     * @param client the client that is showing this page
     */
    protected ConnectionPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw() {
        showGUIPage("Connection", "/fxml/ConnectionPage.fxml", new ConnectionPageController());
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.page.AbstractMenuPage;

public class ConnectionPageGUI extends AbstractConnectionPage {

    protected ConnectionPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Connection", "/fxml/ConnectionPage.fxml");
    }
}

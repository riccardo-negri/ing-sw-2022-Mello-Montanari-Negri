package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMenuPage;
public class MenuPageGUI extends AbstractMenuPage {

    protected MenuPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Menu", "/fxml/MenuPage.fxml");
    }
}

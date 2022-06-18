package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;

public class CreateGamePageGUI extends AbstractCreateGamePage {

    protected CreateGamePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Create Game", "/fxml/CreateGamePage.fxml");
    }
}

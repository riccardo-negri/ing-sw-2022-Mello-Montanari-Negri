package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;
import it.polimi.ingsw.client.ui.gui.controllers.CreateGamePageController;

public class CreateGamePageGUI extends AbstractCreateGamePage {

    protected CreateGamePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Create Game", "/fxml/CreateGamePage.fxml", new CreateGamePageController());
    }
}

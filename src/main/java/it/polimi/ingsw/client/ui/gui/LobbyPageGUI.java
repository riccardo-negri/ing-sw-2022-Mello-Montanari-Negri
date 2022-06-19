package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;
import it.polimi.ingsw.client.page.AbstractLobbyPage;
import it.polimi.ingsw.client.ui.gui.controllers.LobbyPageController;

public class LobbyPageGUI extends AbstractLobbyPage {

    protected LobbyPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Lobbies", "/fxml/LobbyPage.fxml", new LobbyPageController());
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractLobbySelectionPages;
import it.polimi.ingsw.client.ui.gui.controllers.LobbySelectionPageController;

public class LobbySelectionPageGUI extends AbstractLobbySelectionPages {

    protected LobbySelectionPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Open Lobbies", "/fxml/LobbySelectionPage.fxml", new LobbySelectionPageController());
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractLobbySelectionPages;
import it.polimi.ingsw.client.ui.gui.controllers.LobbySelectionPageController;

public class LobbySelectionPageGUI extends AbstractLobbySelectionPages {

    /**
     * pass client to AbstractLobbySelectionPages constructor
     * @param client the client that is showing this page
     */
    protected LobbySelectionPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw() {
        showGUIPage("Open Lobbies", "/fxml/LobbySelectionPage.fxml", new LobbySelectionPageController());
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractLobbyPage;
import it.polimi.ingsw.client.ui.gui.controllers.LobbyPageController;

public class LobbyPageGUI extends AbstractLobbyPage {

    /**
     * pass client to AbstractLobbyPage constructor
     * @param client the client that is showing this page
     */
    protected LobbyPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw() {
        showGUIPage("Lobbies", "/fxml/LobbyPage.fxml", new LobbyPageController());
    }
}

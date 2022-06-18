package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;

public class LobbyPageGUI extends AbstractCreateGamePage {

    protected LobbyPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Lobbies", "/fxml/LobbyPage.fxml");
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.page.AbstractEndPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.client.ui.gui.controllers.EndPageController;

public class BoardPageGUI extends AbstractBoardPage {

    protected BoardPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Game", "/fxml/BoardPage.fxml", new BoardPageController());
    }
}

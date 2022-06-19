package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractEndPage;
import it.polimi.ingsw.client.page.AbstractStorylinePage;
import it.polimi.ingsw.client.ui.gui.controllers.EndPageController;

public class EndPageGUI extends AbstractEndPage {

    protected EndPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Game Ended", "/fxml/EndPage.fxml", new EndPageController());
    }
}

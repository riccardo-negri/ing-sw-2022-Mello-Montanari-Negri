package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreditsPage;
import it.polimi.ingsw.client.ui.gui.controllers.CreditsPageController;

public class CreditsPageGUI extends AbstractCreditsPage {

    /**
     * pass client to AbstractCreditsPage constructor
     * @param client the client that is showing this page
     */
    protected CreditsPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Credits", "/fxml/CreditsPage.fxml", new CreditsPageController());
    }
}

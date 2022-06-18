package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractStorylinePage;
import it.polimi.ingsw.client.page.AbstractWelcomePage;

public class StorylinePageGUI extends AbstractStorylinePage {

    protected StorylinePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Storyline!", "/fxml/StorylinePage.fxml");
    }
}

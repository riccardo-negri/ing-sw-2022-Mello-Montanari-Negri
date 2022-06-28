package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractStorylinePage;
import it.polimi.ingsw.client.ui.gui.controllers.StorylinePageController;

/**
 * GUI implementation of the StorylinePage
 */
public class StorylinePageGUI extends AbstractStorylinePage {

    /**
     * pass client to AbstractStorylinePage constructor
     * @param client the client that is showing this page
     */
    protected StorylinePageGUI(Client client) {
        super(client);
    }

    /**
     * load the fxml file and give the correct controller and title to it
     */
    @Override
    public void draw() {
        showGUIPage("Storyline!", "/fxml/StorylinePage.fxml", new StorylinePageController());
    }
}

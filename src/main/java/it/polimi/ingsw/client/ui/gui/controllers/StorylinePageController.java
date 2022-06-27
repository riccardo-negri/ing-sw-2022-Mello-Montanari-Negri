package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractStorylinePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StorylinePageController extends AbstractController{

    /**
     * close the current page and go back to main menu
     * @param event the button click event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        ((AbstractStorylinePage) client.getCurrState()).onEnd();
        client.drawNextPage();
    }
}
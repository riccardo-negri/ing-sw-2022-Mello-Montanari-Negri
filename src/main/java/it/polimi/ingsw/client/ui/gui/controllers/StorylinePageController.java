package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractStorylinePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StorylinePageController extends AbstractController{

    @FXML
    private void handleBack(ActionEvent event) {
        ((AbstractStorylinePage) client.getCurrState()).onEnd();
        client.drawNextPage();
    }
}
package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractCreditsPage;
import it.polimi.ingsw.client.page.AbstractStorylinePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CreditsPageController extends AbstractController{

    @FXML
    private void handleBack(ActionEvent event) {
        ((AbstractCreditsPage) client.getCurrState()).onEnd();
        client.drawNextPage();
    }
}
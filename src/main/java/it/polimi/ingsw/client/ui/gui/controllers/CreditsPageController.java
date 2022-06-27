package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractCreditsPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CreditsPageController extends AbstractController{

    /**
     * close the current page and go back to main menu
     * @param event the button click event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        ((AbstractCreditsPage) client.getCurrState()).onEnd();
        client.drawNextPage();
    }
}
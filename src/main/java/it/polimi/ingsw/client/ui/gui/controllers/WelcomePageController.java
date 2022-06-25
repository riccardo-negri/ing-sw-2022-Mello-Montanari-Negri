package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.ui.gui.WelcomePageGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WelcomePageController extends AbstractController{

    /**
     * go to main menu page
     * @param event the button click event
     */
    @FXML
    private void handleContinue(ActionEvent event) {
        ((WelcomePageGUI) client.getCurrState()).onEnd(true);
        client.drawNextPage();
    }
}
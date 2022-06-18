package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.ui.gui.WelcomePageGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WelcomePageController extends AbstractController{

    @FXML
    private void handleContinue(ActionEvent event) {
        ((WelcomePageGUI) client.getCurrState()).onEnd(true);
        client.drawNextPage();
    }
}
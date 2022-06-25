package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractMenuPage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuPageController extends AbstractController {
    /**
     * go to connection page
     * @param event the button click event
     */
    @FXML
    private void handlePlay(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(1);
        client.drawNextPage();
    }

    /**
     * go to story line page
     * @param event the button click event
     */
    @FXML
    private void handleStoryline(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(2);
        client.drawNextPage();
    }

    /**
     * go to credits page
     * @param event the button click event
     */
    @FXML
    private void handleCredits(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(3);
        client.drawNextPage();
    }

    /**
     * close the window and terminate the program
     * @param event the button click event
     */
    @FXML
    private void handleQuit(ActionEvent event) {
        Platform.exit();
    }
}
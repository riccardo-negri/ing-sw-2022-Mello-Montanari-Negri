package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractMenuPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MenuPageController extends AbstractController {
    @FXML
    private void handlePlay(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(1);
        client.drawNextPage();
    }

    @FXML
    private void handleCredits(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(1);
        client.drawNextPage();
    }


    @FXML
    private void handleStoryline(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(3);
        client.drawNextPage();
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        ((AbstractMenuPage) client.getCurrState()).onEnd(4);
        client.drawNextPage();
    }
}
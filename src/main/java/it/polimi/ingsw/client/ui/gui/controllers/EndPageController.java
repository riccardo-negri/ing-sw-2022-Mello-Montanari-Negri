package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractEndPage;
import it.polimi.ingsw.client.page.AbstractStorylinePage;
import it.polimi.ingsw.model.entity.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;

public class EndPageController extends AbstractController{


    @FXML
    private void handleContinue(ActionEvent event) {
        ((AbstractEndPage) client.getCurrState()).onEnd();
        client.drawNextPage();
    }

    @FXML
    void initialize() {
        /*Game model = client.getModel();
        if (model.getWinner() == model.getWizard(client.getUsernames().indexOf(client.getUsername())).getTowerColor()) {

        } else {

        }*/
    }
}
package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractEndPage;
import it.polimi.ingsw.model.entity.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;

/**
 * controller for END state
 */
public class EndPageController extends AbstractController{

    @FXML
    Label result;

    @FXML
    Label info;

    /**
     * close the current page and go back to main menu
     * @param event the button click event
     */
    @FXML
    private void handleContinue(ActionEvent event) {
        ((AbstractEndPage) client.getCurrState()).onEnd();
        client.drawNextPage();
    }

    /**
     * show resign related message if someone resigned or, if it finished normally, the result of the game
     */
    @FXML
    void initialize() {
        Game model = client.getModel();
        if(model.getWinner() == null) {
            result.setText("USER RESIGNED");
            if (client.getResigned().equals(client.getUsername()))
                info.setText("You resigned from the game.");
            else
                info.setText(client.getResigned() + " resigned from the game.");
            client.setResigned(null);  // reset the default value for the next game
        } else {
            if (model.getWinner() == model.getWizard(client.getUsernames().indexOf(client.getUsername())).getTowerColor())
                result.setText("YOU WON!");
            else
                result.setText("YOU LOST!");
        }
    }
}
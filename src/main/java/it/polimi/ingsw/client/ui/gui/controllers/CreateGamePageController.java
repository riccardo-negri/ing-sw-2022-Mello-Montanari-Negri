package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractCreateGamePage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * controller for CREATE_GAME state
 */
public class CreateGamePageController extends AbstractController{

    @FXML
    Spinner<Integer> playersNumber;
    @FXML
    CheckBox advancedRules;

    /**
     * set boundaries and initial value for the playersNumber spinner
     */
    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4,2);
        playersNumber.setValueFactory(valueFactory);
    }

    /**
     * retrieve game information from the input fields, ask the server to create a game and got to lobby page
     * @param event the button click event
     */
    @FXML
    private void handleCreate(ActionEvent event) {
        AbstractCreateGamePage page = (AbstractCreateGamePage) client.getCurrState();
        int pn = playersNumber.getValue();
        page.createGameAndGoToLobby(pn, advancedRules.isSelected());
        page.onEnd();
        client.drawNextPage();
    }
}
package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class CreateGamePageController extends AbstractController{

    @FXML
    Spinner<Integer> playersNumber;
    @FXML
    CheckBox advancedRules;

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4,2);
        playersNumber.setValueFactory(valueFactory);
    }

    @FXML
    private void handleCreate(ActionEvent event) {
        AbstractCreateGamePage page = (AbstractCreateGamePage) client.getCurrState();
        int pn = playersNumber.getValue();
        page.createGameAndGoToLobby(pn, advancedRules.isSelected());
        page.onEnd();
        client.drawNextPage();
    }
}
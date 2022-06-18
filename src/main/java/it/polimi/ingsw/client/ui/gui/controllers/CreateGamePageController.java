package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class CreateGamePageController extends AbstractController{

    @FXML
    TextField playersNumber;
    @FXML
    CheckBox advancedRules;

    @FXML
    private void handleCreate(ActionEvent event) {
        AbstractCreateGamePage page = (AbstractCreateGamePage) client.getCurrState();
        int pn = Integer.parseInt(playersNumber.getText());
        page.createGameAndGoToLobby(pn, advancedRules.isSelected());
        page.onEnd();
        client.drawNextPage();
    }
}
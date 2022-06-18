package it.polimi.ingsw.client.ui.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;

public class LobbyPageController extends AbstractController{
    @FXML
    private Label numPlayersLabel;

    @FXML
    private Label advancedRulesField;

    @FXML
    private ColumnConstraints connectedUsers;

    @FXML
    void initialize() {
        numPlayersLabel.setText(String.valueOf(client.getPlayerNumber()));
        advancedRulesField.setText(client.isAdvancedGame() ? "Advanced" : "Simple");
    }
}

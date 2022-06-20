package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractLobbyPage;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.Message;
import it.polimi.ingsw.networking.UserConnected;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.text.Text;

public class LobbyPageController extends AbstractController{
    @FXML
    private Label numPlayersLabel;

    @FXML
    private Label advancedRulesLabel;
    @FXML
    private Label fieldName1;
    @FXML
    private Label fieldName2;

    @FXML
    private ColumnConstraints connectedUsers;

    @FXML
    private Text connected;

    @FXML
    void initialize() {
        if (client.getPlayerNumber() != 0) {
            numPlayersLabel.setText(String.valueOf(client.getPlayerNumber()));
            advancedRulesLabel.setText(client.isAdvancedGame() ? "Advanced" : "Simple");
        }
        else {
            numPlayersLabel.setText("");
            advancedRulesLabel.setText("");
            fieldName1.setText("");
            fieldName2.setText("");
        }
        client.getConnection().bindFunctionAndTestPrevious(this::onNewMessage);
    }

    boolean onNewMessage(Connection c) {
        Message m = c.getFirstMessage();
        if (m instanceof UserConnected uc) {
            connected.setText(connected.getText() + uc.username() + "\n");
            return true;
        } else if (m instanceof InitialState is) {
            int id = Game.deserializeGameFromString(is.getState());
            client.setModel(Game.request(id));
            client.setUsernames(is.getUsernames());
            ((AbstractLobbyPage) client.getCurrState()).onEnd();
            Platform.runLater(()-> client.drawNextPage());
            return true;
        }
        return false;
    }
}

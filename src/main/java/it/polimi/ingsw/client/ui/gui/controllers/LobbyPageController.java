package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.page.AbstractLobbyPage;
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
    private Label advancedRulesField;

    @FXML
    private ColumnConstraints connectedUsers;

    @FXML
    private Text connected;

    @FXML
    void initialize() {
        numPlayersLabel.setText(String.valueOf(client.getPlayerNumber()));
        advancedRulesField.setText(client.isAdvancedGame() ? "Advanced" : "Simple");
        client.getConnection().bindFunctionAndTestPrevious(this::onNewMessage);
    }

    boolean onNewMessage(Connection c) {
        Message m = c.getLastMessage();
        if (m instanceof UserConnected uc) {
            connected.setText(connected.getText() + "\n" + uc.username());
            return true;
        } else if (m instanceof InitialState is) {
            ((AbstractLobbyPage) client.getCurrState()).onEnd();
            Platform.runLater(()->{client.drawNextPage();});
            return true;
        }
        return false;
    }
}

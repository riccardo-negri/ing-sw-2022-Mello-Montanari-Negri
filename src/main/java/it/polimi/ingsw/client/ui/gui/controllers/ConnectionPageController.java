package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.logging.Level;

import static it.polimi.ingsw.client.page.ClientPage.LOBBY_PAGE;

public class ConnectionPageController extends AbstractController{

    @FXML
    TextField username;
    @FXML
    TextField port;
    @FXML
    TextField ip;
    @FXML
    GridPane askCreateSection;

    @FXML
    Text messageLabel;

    @FXML void initialize() {
        if (client.isJustDisconnected()) {
            messageLabel.setText("You got disconnected from the game, rejoin the game with the same username");
            client.setJustDisconnected(false);
        }
    }

    @FXML
    private void handleConnect(ActionEvent event) {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        int p = Integer.parseInt(port.getText());
        boolean usernameAvailable = true;
        try {
            page.connectToMatchmakingServer(ip.getText(), p, username.getText());
            usernameAvailable = page.waitForLobbiesListOrRedirect();
        }
        catch (Exception e) {
            client.getLogger().log(Level.SEVERE, "Got an exception");
        }

        if (usernameAvailable) {
            if (client.getNextState() == LOBBY_PAGE) {
                client.drawNextPage();
            } else {
                askCreateSection.setVisible(true);
                messageLabel.setText("Do you want to create a new game?");
            }
        } else {
            messageLabel.setText("This username is already taken, change it and try again");
        }
    }

    @FXML
    private void handleAnswerNo() {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        page.onEnd(false);
        client.drawNextPage();
    }

    @FXML
    private void handleAnswerYes() {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        page.onEnd(true);
        client.drawNextPage();
    }
}
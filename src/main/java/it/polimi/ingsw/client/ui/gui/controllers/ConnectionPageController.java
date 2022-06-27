package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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

    /**
     * show disconnected message if it is coming from board page
     */
    @FXML void initialize() {
        if (client.isJustDisconnected()) {
            messageLabel.setText("You got disconnected from the game, rejoin the game with the same username");
            client.setJustDisconnected(false);
        }
    }

    /**
     * connect to the matchmaking server and if the user has already a running game go to lobby page
     * else show the choice between create and join a game
     * @param event the button click event
     */
    @FXML
    private void handleConnect(ActionEvent event) {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        int p = Integer.parseInt(port.getText());
        boolean usernameAvailable;
        try {
            page.connectToMatchmakingServer(ip.getText(), p, username.getText());
            usernameAvailable = page.waitForLobbiesListOrRedirect();
        }
        catch (Exception e) {
            messageLabel.setText("Failed to connect to the server");
            return;
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

    /**
     * got to lobby selection page
     */
    @FXML
    private void handleAnswerNo() {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        page.onEnd(false);
        client.drawNextPage();
    }

    /**
     * go to create game page
     */
    @FXML
    private void handleAnswerYes() {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        page.onEnd(true);
        client.drawNextPage();
    }
}
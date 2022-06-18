package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.logging.Level;

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
    private void handleConnect(ActionEvent event) {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        int p = Integer.parseInt(port.getText());
        try {
            page.connectToMatchmakingServer(ip.getText(), p, username.getText());
            page.waitForLobbiesListOrRedirect();
        }
        catch (Exception e) {
            client.getLogger().log(Level.SEVERE, "Got an exception");
        }

        askCreateSection.setVisible(true);
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
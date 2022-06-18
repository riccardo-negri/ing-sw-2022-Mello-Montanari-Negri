package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.ui.gui.WelcomePageGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionPageController extends AbstractController{

    @FXML
    TextField username;
    @FXML
    TextField port;
    @FXML
    TextField ip;

    @FXML
    private void handleConnect(ActionEvent event) {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        int p = Integer.parseInt(port.getText());
        page.connectToMatchmakingServer(ip.getText(), p, username.getText());
        System.out.println("connected");

    }
}
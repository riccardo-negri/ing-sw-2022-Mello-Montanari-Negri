package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.WelcomePageGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomePageController {

    public Client client;

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        ((WelcomePageGUI) client.getCurrState()).onEnd(true);
        client.drawNextPage();
    }
}
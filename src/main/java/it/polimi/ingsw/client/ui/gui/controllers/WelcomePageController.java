package it.polimi.ingsw.client.ui.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.WelcomePageGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomePageController {

    static public Client client;
    static public WelcomePageGUI welcomePageGUI;

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        welcomePageGUI.onEnd(true);
        client.drawNextPage();
    }
}
package it.polimi.ingsw.client.ui.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuPageController {

    @FXML
    private Label label;

    @FXML
    private void handlePlay(ActionEvent event) {
        System.out.println("play!");
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        System.out.println("credits!");
    }

    @FXML
    private void handleQuit(ActionEvent event) {
        System.out.println("quit!");
    }

    @FXML
    private void handleStoryline(ActionEvent event) {
        System.out.println("story!");
    }
}
package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIApplication extends Application {
    static Client client;
    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        client = new Client(stage);
        client.start();
    }
}

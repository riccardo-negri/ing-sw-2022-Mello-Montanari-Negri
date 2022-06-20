package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIApplication extends Application {
    static Client client;
    public static void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        client = new Client(stage);
        client.start();
    }
}

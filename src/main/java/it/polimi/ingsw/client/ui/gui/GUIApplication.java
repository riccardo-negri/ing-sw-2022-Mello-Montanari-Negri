package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX Application class
 */
public class GUIApplication extends Application {

    /**
     * launch the graphic application
     * @param args the command line arguments passed when starting the program
     */
    public static void run(String[] args) {
        launch(args);
    }

    /**
     * create and start a client
     * @param stage the stage that the graphic application is showing
     */
    @Override
    public void start(Stage stage) {
        Client client = new Client(stage);
        client.start();
    }
}

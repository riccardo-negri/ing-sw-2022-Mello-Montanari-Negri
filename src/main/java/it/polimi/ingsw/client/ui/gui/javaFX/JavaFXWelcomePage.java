package it.polimi.ingsw.client.ui.gui.javaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class JavaFXWelcomePage extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("/home/pietromello/HardDisk/Università/Semestre 6/Progetto Ingegneria Software/ing-sw-2022-Mello-Montanari-Negri/src/main/java/it/polimi/ingsw/client/ui/gui/fxml/welcomePage.fxml"));
        VBox vbox = loader.<VBox>load();

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }
}

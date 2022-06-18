package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WelcomePageGUI extends AbstractWelcomePage{

    protected WelcomePageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        Stage stage = ((GUI) client.getUI()).getStage();
        client.setNextState(null);
        stage.setTitle("Eriantys");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/WelcomePage.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}

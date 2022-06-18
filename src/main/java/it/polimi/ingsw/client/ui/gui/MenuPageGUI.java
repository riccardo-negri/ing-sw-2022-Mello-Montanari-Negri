package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMenuPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPageGUI extends AbstractMenuPage {

    protected MenuPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        Stage stage = ((GUI) client.getUI()).getStage();
        client.setNextState(null);
        stage.setTitle("Menu");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MenuPage.fxml"));
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

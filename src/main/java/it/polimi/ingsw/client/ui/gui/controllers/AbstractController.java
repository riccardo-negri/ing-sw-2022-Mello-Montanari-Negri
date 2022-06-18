package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.Client;
import javafx.stage.Stage;

public class AbstractController {
    protected Client client;
    protected Stage stage;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

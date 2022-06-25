package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.Client;
import javafx.stage.Stage;

public class AbstractController {
    protected Client client;
    protected Stage stage;

    /**
     * set client value
     * @param client the client is showing this page
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * get client value
     * @param stage the stage of the graphic application that is showing this page
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.Client;

public class AbstractController {
    protected Client client;

    public void setClient(Client client) {
        this.client = client;
    }
}

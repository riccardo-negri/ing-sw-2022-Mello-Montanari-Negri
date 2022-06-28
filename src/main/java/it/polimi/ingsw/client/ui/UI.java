package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;

/**
 * generic User Interface
 */
public interface UI {
    AbstractPage getState (Client client, ClientPage nextState);
}

package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.pages.AbstractClientState;
import it.polimi.ingsw.client.pages.ClientState;

public interface UI {
    AbstractClientState getState (Client client, ClientState nextState);
}

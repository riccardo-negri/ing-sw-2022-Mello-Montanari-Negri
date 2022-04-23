package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.states.AbstractClientState;
import it.polimi.ingsw.client.states.ClientState;

public interface UI {
    AbstractClientState getState (Client client, ClientState nextState);
}

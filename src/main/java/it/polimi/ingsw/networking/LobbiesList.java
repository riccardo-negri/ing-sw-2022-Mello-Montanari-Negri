package it.polimi.ingsw.networking;

import java.util.ArrayList;
import java.util.List;

public class LobbiesList implements Message{
    final ArrayList<LobbyDescriptor> lobbies = new ArrayList<>();

    /**
     * create a message containing available lobbies list
     * @param lobbies list af lobby descriptor for each lobby a user can connect to
     */
    public LobbiesList(List<LobbyDescriptor> lobbies) {
        this.lobbies.addAll(lobbies);
    }

    /**
     * get lobbies value
     * @return list af lobby descriptor for each lobby a user can connect to
     */
    public List<LobbyDescriptor> getLobbies() {
        return lobbies;
    }
}

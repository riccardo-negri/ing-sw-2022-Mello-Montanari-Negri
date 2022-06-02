package it.polimi.ingsw.networking;

import java.util.ArrayList;
import java.util.List;

public class LobbiesList implements Message{
    final ArrayList<LobbyDescriptor> lobbies = new ArrayList<>();

    public LobbiesList(List<LobbyDescriptor> lobbies) {
        this.lobbies.addAll(lobbies);
    }

    public List<LobbyDescriptor> getLobbies() {
        return lobbies;
    }
}

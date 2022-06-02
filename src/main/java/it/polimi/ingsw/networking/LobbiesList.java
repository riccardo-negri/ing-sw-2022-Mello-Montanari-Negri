package it.polimi.ingsw.networking;

import java.util.ArrayList;
import java.util.List;

public class LobbiesList extends Message{
    ArrayList<LobbyDescriptor> lobbies = new ArrayList<>();

    public LobbiesList(List<LobbyDescriptor> lobbies) {
        this.lobbies.addAll(lobbies);
    }

    public ArrayList<LobbyDescriptor> getLobbies() {
        return lobbies;
    }
}

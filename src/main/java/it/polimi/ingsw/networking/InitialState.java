package it.polimi.ingsw.networking;

import java.util.ArrayList;
import java.util.List;

public class InitialState implements Message {
    private final String state;

    // the list of the usernames of the wizards ordered by wizard id
    // this is how the client finds out which wizard he belongs to
    private final ArrayList<String> usernames = new ArrayList<>();

    public InitialState(String state, List<String> usernames) {
        this.state = state;
        this.usernames.addAll(usernames);
    }

    public String getState() {
        return state;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public int idFromUsername(String name) {
        return usernames.indexOf(name);
    }
}

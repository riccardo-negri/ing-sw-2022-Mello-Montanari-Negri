package it.polimi.ingsw.networking;

import java.util.ArrayList;
import java.util.List;

/**
 * a message sent from the server to the client when moving from the lobby to the game, contains the current state
 * of the game, also communicates to the client that the game is started
 */
public class InitialState implements Message {
    private final String state;

    // the list of the usernames of the wizards ordered by wizard id
    // this is how the client finds out which wizard he belongs to
    private final ArrayList<String> usernames = new ArrayList<>();

    /**
     * creates a message that contains a serialized game
     * @param state the string containing game json
     * @param usernames the list of usernames corresponding to each wizard, respecting wizard index order
     */
    public InitialState(String state, List<String> usernames) {
        this.state = state;
        this.usernames.addAll(usernames);
    }

    /**
     * get state value
     * @return the string containing game json
     */
    public String getState() {
        return state;
    }

    /**
     * get usernames value
     * @return the list of usernames corresponding to each wizard, respecting wizard index order
     */
    public List<String> getUsernames() {
        return usernames;
    }
}

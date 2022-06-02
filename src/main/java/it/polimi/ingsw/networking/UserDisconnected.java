package it.polimi.ingsw.networking;

public class UserDisconnected implements Message {
    private final String username;

    public UserDisconnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

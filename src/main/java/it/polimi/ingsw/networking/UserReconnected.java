package it.polimi.ingsw.networking;

public class UserReconnected implements Message {
    private final String username;

    public UserReconnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

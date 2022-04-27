package it.polimi.ingsw.utils;

public class UserDisconnected extends MessageContent {
    private final String username;

    public UserDisconnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

package it.polimi.ingsw.utils;

public class UserReconnected extends MessageContent {
    private final String username;

    public UserReconnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

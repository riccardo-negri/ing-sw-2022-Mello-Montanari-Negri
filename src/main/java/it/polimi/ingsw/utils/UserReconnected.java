package it.polimi.ingsw.utils;

public class UserReconnected extends Message {
    private final String username;

    public UserReconnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

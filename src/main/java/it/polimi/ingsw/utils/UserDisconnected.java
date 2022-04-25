package it.polimi.ingsw.utils;

public class UserDisconnected extends Message {
    private final String username;

    public UserDisconnected(String username) {
        this.username = username;
    }

}

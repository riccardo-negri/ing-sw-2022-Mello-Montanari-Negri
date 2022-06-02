package it.polimi.ingsw.networking;

public class UserConnected implements Message {
    private final String username;

    public UserConnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

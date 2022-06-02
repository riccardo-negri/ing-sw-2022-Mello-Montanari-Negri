package it.polimi.ingsw.networking;

public class UserConnected extends Message {
    private final String username;

    public UserConnected(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

package it.polimi.ingsw.networking;

public class UserResigned extends Message {
    private String username;

    public UserResigned(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
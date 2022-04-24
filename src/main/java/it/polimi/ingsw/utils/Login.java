package it.polimi.ingsw.utils;

public class Login extends Message{
    final String username;
    final int playerNumber;
    final boolean advancedRules;

    public Login(String username, int playerNumber, boolean advancedRules) {
        this.username = username;
        this.playerNumber = playerNumber;
        this.advancedRules = advancedRules;
    }

    public String getUsername() {
        return username;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isAdvancedRules() {
        return advancedRules;
    }
}

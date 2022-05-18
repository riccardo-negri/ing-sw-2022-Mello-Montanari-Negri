package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;

public class Login extends Message {
    final String username;
    final PlayerNumber playerNumber;
    final GameMode mode;

    public Login(String username, PlayerNumber playerNumber, GameMode advancedRules) {
        this.username = username;
        this.playerNumber = playerNumber;
        this.mode = advancedRules;
    }

    public String getUsername() {
        return username;
    }

    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    public GameMode getGameMode() {
        return mode;
    }
}
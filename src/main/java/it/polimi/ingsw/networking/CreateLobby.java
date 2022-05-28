package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;

public class CreateLobby extends Message {

    final PlayerNumber playerNumber;
    final GameMode mode;

    public CreateLobby(PlayerNumber playerNumber, GameMode advancedRules) {
        this.playerNumber = playerNumber;
        this.mode = advancedRules;
    }


    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    public GameMode getGameMode() {
        return mode;
    }
}

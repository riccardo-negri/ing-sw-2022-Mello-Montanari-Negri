package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;

/**
 * a message that requests to the server to create a new lobby with the desired parameters
 */
public class CreateLobby implements Message {

    final PlayerNumber playerNumber;
    final GameMode mode;

    /**
     * creates a message to request lobby creation
     * @param playerNumber the number of players in the new lobby
     * @param advancedRules the game mode to use in the new lobby
     */
    public CreateLobby(PlayerNumber playerNumber, GameMode advancedRules) {
        this.playerNumber = playerNumber;
        this.mode = advancedRules;
    }

    /**
     * get playerNumber value
     * @return the number of players in the new lobby
     */
    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    /**
     * get mode value
     * @return the game mode to use in the new lobby
     */
    public GameMode getGameMode() {
        return mode;
    }
}

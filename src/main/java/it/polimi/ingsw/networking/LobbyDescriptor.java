package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * it's a serializable container for lobby information used in the message lobbies list
 */
public class LobbyDescriptor implements Serializable {
    final String code;
    final PlayerNumber playerNumber;
    final GameMode gameMode;

    final ArrayList<String> connected = new ArrayList<>();

    /**
     * save all values in the container
     * @param code the code that identifies the lobby
     * @param playerNumber the maximum number of players accepted from the lobby
     * @param gameMode the game mode of the game
     * @param connected list of usernames of connected users
     */
    public LobbyDescriptor(String code, PlayerNumber playerNumber, GameMode gameMode, List<String> connected) {
        this.code = code;
        this.playerNumber = playerNumber;
        this.gameMode = gameMode;
        this.connected.addAll(connected);
    }

    /**
     * get code value
     * @return the code that identifies the lobby
     */
    public String getCode() {
        return code;
    }

    /**
     * get playerNumber value
     * @return the maximum number of players accepted from the lobby
     */
    public PlayerNumber getPlayerNumber () {
        return playerNumber;
    }

    /**
     * get gameMode value
     * @return the game mode of the game
     */
    public GameMode getGameMode () {
        return gameMode;
    }

    /**
     * get connected value
     * @return list of usernames of connected users
     */
    public List<String> getConnected () {
        return connected;
    }

    /**
     * test for equality of the lobby descriptor by testing for equality of its four components
     * @param obj the object to test for equality against
     * @return if the two objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LobbyDescriptor o) {
            return code.equals(o.code) && playerNumber.equals(o.playerNumber) && gameMode.equals(o.gameMode) &&
                    connected.equals(o.connected);
        }
        return false;
    }
}

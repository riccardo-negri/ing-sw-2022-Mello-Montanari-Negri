package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LobbyDescriptor implements Serializable {
    final String code;
    final PlayerNumber playerNumber;
    final GameMode gameMode;

    final ArrayList<String> connected = new ArrayList<>();

    public LobbyDescriptor(String code, PlayerNumber playerNumber, GameMode gameMode, List<String> connected) {
        this.code = code;
        this.playerNumber = playerNumber;
        this.gameMode = gameMode;
        this.connected.addAll(connected);
    }

    public String getCode() {
        return code;
    }

    public PlayerNumber getPlayerNumber () {
        return playerNumber;
    }

    public GameMode getGameMode () {
        return gameMode;
    }

    public List<String> getConnected () {
        return connected;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LobbyDescriptor o) {
            return code.equals(o.code) && playerNumber.equals(o.playerNumber) && gameMode.equals(o.gameMode) &&
                    connected.equals(o.connected);
        }
        return false;
    }
}

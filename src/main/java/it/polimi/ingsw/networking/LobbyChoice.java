package it.polimi.ingsw.networking;

public class LobbyChoice extends Message {
    final String code;

    public LobbyChoice(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

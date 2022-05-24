package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;

import java.util.Arrays;
import java.util.List;

public class SavedGameRecord {
    private final Game game;
    private final List<String> usernames;

    public SavedGameRecord(String gameString, String usernamesString) throws Exception {
        Integer id = Game.deserializeGameFromString(gameString);
        this.game = Game.request(id);
        this.usernames = Arrays.stream(usernamesString.split("\n")).toList();
        if (usernames.size() != game.getPlayerNumber().getWizardNumber())
            throw new Exception("Bad record format");
    }

    public Game getGame() {
        return game;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}

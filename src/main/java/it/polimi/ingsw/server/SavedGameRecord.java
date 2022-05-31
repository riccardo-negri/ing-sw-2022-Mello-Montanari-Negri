package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;

import java.util.Arrays;
import java.util.List;

public class SavedGameRecord {
    private final Game game;
    private final List<String> usernames;

    private final GameSavesManager savesManager;

    public SavedGameRecord(String gameString, String usernamesString, GameSavesManager savesManager) throws Exception {
        Integer id = Game.deserializeGameFromString(gameString);
        this.game = Game.request(id);
        this.usernames = Arrays.stream(usernamesString.split("\n")).toList();
        this.savesManager = savesManager;
        if (usernames.size() != game.getPlayerNumber().getWizardNumber())
            throw new Exception("Bad record format");
    }

    public Game getGame() {
        return game;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public GameSavesManager getSavesManager() {
        return savesManager;
    }
}

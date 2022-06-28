package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;

import java.util.Arrays;
import java.util.List;

/**
 * a container for the data retrieved from a game save
 */
public class SavedGameRecord {
    private final Game game;
    private final List<String> usernames;

    private final GameSavesManager savesManager;

    /**
     * converts the text read from the save files to game related data
     * @param gameString content of the snapshot file in the game save folder
     * @param usernamesString content of the username file in the game save folder
     * @param savesManager the game save manager operating on this save folder
     * @throws BadRecordException if the game save folder is not structured correctly
     */
    public SavedGameRecord(String gameString, String usernamesString, GameSavesManager savesManager) throws BadRecordException {
        Integer id = Game.deserializeGameFromString(gameString);
        this.game = Game.request(id);
        this.usernames = Arrays.stream(usernamesString.split("\n")).toList();
        this.savesManager = savesManager;
        if (usernames.size() != game.getPlayerNumber().getWizardNumber())
            throw new BadRecordException();
    }

    /**
     * get game value
     * @return the game created by deserializing the snapshot
     */
    public Game getGame() {
        return game;
    }

    /**
     * get usernames value
     * @return the list of usernames created by parsing the usernames file
     */
    public List<String> getUsernames() {
        return usernames;
    }

    /**
     * get savesManager value
     * @return the game save manager operating on this save folder
     */
    public GameSavesManager getSavesManager() {
        return savesManager;
    }
}

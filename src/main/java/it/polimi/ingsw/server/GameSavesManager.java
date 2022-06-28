package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.utils.Counter;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

/**
 * an instance is bounded to a specific game save folder and offers the methods to manage the saves files
 */
public class GameSavesManager extends SavesManager {
    // counter always contains the value of the next snapshot id
    final Counter counter;

    final String code;

    final boolean restored;

    /**
     * create a manager for a specific game folder starting from the first snapshot
     * @param logger the eventual filesystem errors are sent to log
     * @param code the string that identifies the game folder (is the same used for the lobby)
     */
    public GameSavesManager(Logger logger, String code) {
        super(logger);
        this.code = code;
        counter = new Counter();
        restored = false;
    }

    /**
     * create a manager for a specific game folder starting from a specified snapshot
     * @param logger the eventual filesystem errors are sent to log
     * @param code the string that identifies the game folder (is the same used for the lobby)
     * @param lastSnapshotId the id of the snapshot already created
     */
    public GameSavesManager (Logger logger, String code, int lastSnapshotId) {
        super(logger);
        this.code = code;
        counter = new Counter(lastSnapshotId + 1);
        restored = true;
    }

    /**
     * create a file containing the new game state and remove the previous
     * @param game the game to serialize into a json string
     * @return if the operation on files is successful
     */
    public boolean createSnapshot(Game game) {
        int current = counter.increment() - 1;
        Path snapshotPath = gameFilePath(code, Integer.toString(current));
        boolean success = writeFile(snapshotPath, game.serializeGame());
        if (success) {
            Path oldSnapshot = gameFilePath(code, Integer.toString(current-1));
            deleteFile(oldSnapshot);
        }
        return success;
    }

    /**
     * create a folder with the game code and initialize it with usernames file and first snapshot
     * @param game the game to save in the folder
     * @param usernames the list of usernames connected to each wizard in order of wizard id
     * @return if the operation on the folder is successful
     */
    public boolean createGameFolder(Game game, List<String> usernames) {
        if (restored)
            return true;
        // delete any previous folder for this same id
        // in theory there should be no folder with the same name because they are removed in the beginning
        deleteGameFolder();
        Path folderPath = gameFolderPath(code);
        Path usernamesPath = gameFilePath(code, USERNAMES_FILE_NAME);
        String text = usernames.stream().reduce("", (a, b) -> a + "\n" + b).substring(1);
        if (createFolder(folderPath) && writeFile(usernamesPath, text)) {
            return createSnapshot(game);
        }
        return false;
    }

    /**
     * delete the game folder managed from this saves manager
     */
    public void deleteGameFolder() {
        super.deleteGameFolder(code);
    }

    /**
     * get code value
     * @return the string that identifies the game folder
     */
    public String getCode() {
        return code;
    }
}

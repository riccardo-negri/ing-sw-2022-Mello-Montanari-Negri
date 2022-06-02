package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.utils.Counter;

import java.nio.file.Path;
import java.util.Vector;
import java.util.logging.Logger;

public class GameSavesManager extends SavesManager {
    // counter always contains the value of the next snapshot id
    final Counter counter;

    final String code;

    final boolean restored;

    public GameSavesManager(Logger logger, String code) {
        super(logger);
        this.code = code;
        counter = new Counter();
        restored = false;
    }

    public GameSavesManager (Logger logger, String code, int lastSnapshotId) {
        super(logger);
        this.code = code;
        counter = new Counter(lastSnapshotId + 1);
        restored = true;
    }

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

    public boolean createGameFolder(Game game, Vector<String> usernames) {
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

    public void deleteGameFolder() {
        super.deleteGameFolder(code);
    }

    public String getCode() {
        return code;
    }
}

package it.polimi.ingsw.server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainSavesManager extends SavesManager{

    final List<String> gameCodes = new ArrayList<>();

    public MainSavesManager(Logger logger) {
        super(logger);
    }

    public boolean createSavesFolder() {
        Path path = Paths.get(savesRoot);
        return createFolder(path);
    }

    public List<SavedGameRecord> restoreAll() {
        Path folder = Paths.get(savesRoot);
        File[] files = folder.toFile().listFiles();
        List<SavedGameRecord> result = new ArrayList<>();
        if (files == null) {
            String toLog = "Unable to read " + savesRoot + " directory";
            logger.log(Level.SEVERE, toLog);
            return result;
        }
        for (File f : files) {
            String code = f.getName();
            try {
                result.add(restoreGame(code));
            } catch (Exception e) {
                String toLog = "Skipping bad record " + e.getMessage();
                logger.log(Level.WARNING, toLog);
            }
        }
        return result;
    }

    // restore the saved game and when it's done delete the folder
    public SavedGameRecord restoreGame(String folderCode) throws Exception {
        Path folder = gameFolderPath(folderCode);
        File[] files = folder.toFile().listFiles();
        String usernames = null;
        int maxCount = -1;
        if (files == null || gameCodes.contains(folderCode)) {
            deleteGameFolder(folderCode);
            throw new Exception("Bad record format");
        }
        for (File f : files) {
            if (f.getName().equals(usernamesFileName)) {
                usernames = readFile(f.toPath());
            }
            else {
                int count = Integer.parseInt(f.getName());
                if (count > maxCount)
                    maxCount = count;
            }
        }
        if (maxCount == -1 || usernames == null) {
            deleteGameFolder(folderCode);
            throw new Exception("Bad record format");
        }
        String game = readFile(gameFilePath(folderCode, Long.toString(maxCount)));
        GameSavesManager sm = new GameSavesManager(logger, folderCode, maxCount);
        gameCodes.add(folderCode);
        return new SavedGameRecord(game, usernames, sm);
    }

    GameSavesManager createGameSavesManager() {
        String code;
        do {
            code = randomString();
        } while (gameCodes.contains(code));
        return new GameSavesManager(logger, code);
    }

    static String randomString() {
        Random r = new Random();
        char[] letters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder string = new StringBuilder("#");
        for (int i = 0; i < 3; i++) {
            string.append(letters[r.nextInt(letters.length)]);
        }
        return string.toString();
    }
}

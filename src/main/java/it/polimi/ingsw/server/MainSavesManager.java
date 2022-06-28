package it.polimi.ingsw.server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * offers the methods to manage the main save folder containing alla the individual game folders
 */
public class MainSavesManager extends SavesManager{

    final List<String> gameCodes = new ArrayList<>();
    static final Random r = new Random();

    /**
     * create a manager for the main folder which contains all the games folders
     * @param logger the eventual filesystem errors are sent to log
     */
    public MainSavesManager(Logger logger) {
        super(logger);
    }

    /**
     * create the main folder at the defined location
     * @return if the creation is successful
     */
    public boolean createSavesFolder() {
        Path path = Paths.get(SAVES_ROOT);
        return createFolder(path);
    }

    /**
     * read all the game folders and create a game record for each one
     * @return the list of game records
     */
    public List<SavedGameRecord> restoreAll() {
        File[] files = listDirectory(SAVES_ROOT);
        List<SavedGameRecord> result = new ArrayList<>();
        if (files == null) {
            String toLog = "Unable to read " + SAVES_ROOT + " directory";
            logger.log(Level.SEVERE, toLog);
            return result;
        }
        for (File f : files) {
            String code = f.getName();
            try {
                result.add(restoreGame(code));
            } catch (BadRecordException e) {
                String toLog = "Skipping bad record " + e.getMessage();
                logger.log(Level.WARNING, toLog);
            }
        }
        return result;
    }

    /**
     * read the game folder and create a game record
     * @param folderCode the code of the game to restore
     * @return the game record of this game save
     * @throws BadRecordException if the game save folder is not structured correctly, in this case folder was deleted
     */
    public SavedGameRecord restoreGame(String folderCode) throws BadRecordException {
        Path folder = gameFolderPath(folderCode);
        File[] files = folder.toFile().listFiles();
        String usernames = null;
        // if there are two saves files something happened during the last save and the older is considered more safe
        int minCount = Integer.MAX_VALUE;
        if (files == null || gameCodes.contains(folderCode)) {
            deleteGameFolder(folderCode);
            throw new BadRecordException();
        }
        for (File f : files) {
            if (f.getName().equals(USERNAMES_FILE_NAME)) {
                usernames = readFile(f.toPath());
            }
            else {
                int count = Integer.parseInt(f.getName());
                if (count < minCount)
                    minCount = count;
            }
        }
        if (minCount == Integer.MAX_VALUE || usernames == null) {
            deleteGameFolder(folderCode);
            throw new BadRecordException();
        }
        String game = readFile(gameFilePath(folderCode, Long.toString(minCount)));
        GameSavesManager sm = new GameSavesManager(logger, folderCode, minCount);
        gameCodes.add(folderCode);
        return new SavedGameRecord(game, usernames, sm);
    }

    /**
     * create a game saves manager with a code that is not already in use
     * @return the new game saves manager
     */
    GameSavesManager createGameSavesManager() {
        String code;
        do {
            code = randomString();
        } while (gameCodes.contains(code));
        return new GameSavesManager(logger, code);
    }

    /**
     * generate a random string of a # and 3 other characters
     * @return the 4 characters code generated
     */
    static String randomString() {
        char[] letters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder string = new StringBuilder("#");
        for (int i = 0; i < 3; i++) {
            string.append(letters[r.nextInt(letters.length)]);
        }
        return string.toString();
    }
}

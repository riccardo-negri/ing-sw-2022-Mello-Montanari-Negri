package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavesManager {

    static final String savesRoot = "game_states";

    static boolean createFolder(Logger logger, Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                String toLog = "Unable to create folder: " + e.getMessage();
                logger.log(Level.SEVERE, toLog);
                return false;
            }
        }
        return true;
    }

    static boolean touchFile(Logger logger, Path path) {
        return writeFile(logger, path, "");
    }

    // replaces the file if already exists or creates it otherwise
    static boolean writeFile(Logger logger, Path path, String text) {
        byte[] bytes = text.getBytes();
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            String toLog = "Unable to write file: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
            return false;
        }
        return true;
    }

    public static boolean createSavesFolder(Logger logger) {
        Path path = Paths.get(savesRoot);
        return createFolder(logger, path);
    }

    public static Path gameFolderPath(Game game) {
        return Paths.get(savesRoot, Integer.toString(game.getId()));
    }

    public static Path gameFilePath(Game game, String fileName) {
        return Paths.get(savesRoot, Integer.toString(game.getId()), fileName);
    }

    public static boolean createGameFolder(Logger logger, Game game, Vector<String> usernames) {
        deleteGameFolder(logger, game); // delete any previous folder for this same id
        Path folderPath = gameFolderPath(game);
        Path usernamesPath = gameFilePath(game, "usernames");
        String text = usernames.toString();
        if (createFolder(logger, folderPath)) {
            if (writeFile(logger, usernamesPath, text)) {
                return createSnapshot(logger, game);
            }
        }
        return false;
    }

    public static boolean deleteGameFolder(Logger logger, Game game) {
        Path folderPath = gameFolderPath(game);
        try {
            FileUtils.deleteDirectory(folderPath.toFile());
        } catch (IOException e) {
            String toLog = "Unable to delete folder: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
            return false;
        }
        return true;
    }

    public static boolean createSnapshot(Logger logger, Game game) {
        Date date = new Date();
        long time = date.getTime();
        Path snapshotPath = gameFilePath(game, Long.toString(time));
        return writeFile(logger, snapshotPath, game.serializeGame());
    }
}

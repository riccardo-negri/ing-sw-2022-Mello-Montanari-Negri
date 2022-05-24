package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavesManager {

    static final String savesRoot = "game_states";
    static final String usernamesFileName = "usernames";

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

    static String readFile(Logger logger, Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            String toLog = "Unable to read file: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
            return "";
        }
        return new String(bytes);
    }

    public static boolean createSavesFolder(Logger logger) {
        Path path = Paths.get(savesRoot);
        return createFolder(logger, path);
    }

    public static Path gameFolderPath(int id) {
        return Paths.get(savesRoot, Integer.toString(id));
    }

    public static Path gameFilePath(int id, String fileName) {
        return Paths.get(savesRoot, Integer.toString(id), fileName);
    }

    public static boolean createGameFolder(Logger logger, Game game, Vector<String> usernames) {
        // delete any previous folder for this same id
        // in theory there should be no folder with the same name because they are removed in the beginning
        deleteGameFolder(logger, game.getId());
        Path folderPath = gameFolderPath(game.getId());
        Path usernamesPath = gameFilePath(game.getId(), usernamesFileName);
        String text = usernames.stream().reduce("", (a, b) -> a + "\n" + b).substring(1);
        if (createFolder(logger, folderPath)) {
            if (writeFile(logger, usernamesPath, text)) {
                return createSnapshot(logger, game);
            }
        }
        return false;
    }

    public static List<SavedGameRecord> restoreAll(Logger logger) {
        Path folder = Paths.get(savesRoot);
        File[] files = folder.toFile().listFiles();
        List<SavedGameRecord> result = new ArrayList<>();
        if (files == null) {
            String toLog = "Unable to read " + savesRoot + " directory";
            logger.log(Level.SEVERE, toLog);
            return result;
        }
        for (File f : files) {
            int id = Integer.parseInt(f.getName());
            try {
                result.add(restoreGame(logger, id));
            } catch (Exception e) {
                String toLog = "Skipping bad record " + e.getMessage();
                logger.log(Level.WARNING, toLog);
            }
        }
        return result;
    }

    // restore the saved game and when it's done delete the folder
    public static SavedGameRecord restoreGame(Logger logger, int folderId) throws Exception {
        Path folder = gameFolderPath(folderId);
        File[] files = folder.toFile().listFiles();
        String usernames = null;
        long maxCount = -1;
        if (files == null) {
            deleteGameFolder(logger, folderId);
            throw new Exception("Bad record format");
        }
        for (File f : files) {
            if (f.getName().equals(usernamesFileName)) {
                usernames = readFile(logger, f.toPath());
            }
            else {
                long count = Long.parseLong(f.getName());
                if (count > maxCount)
                    maxCount = count;
            }
        }
        if (maxCount == -1 || usernames == null) {
            deleteGameFolder(logger, folderId);
            throw new Exception("Bad record format");
        }
        String game = readFile(logger, gameFilePath(folderId, Long.toString(maxCount)));
        deleteGameFolder(logger, folderId);
        return new SavedGameRecord(game, usernames);
    }

    public static void deleteGameFolder(Logger logger, int id) {
        Path folderPath = gameFolderPath(id);
        try {
            FileUtils.deleteDirectory(folderPath.toFile());
        } catch (IOException e) {
            String toLog = "Unable to delete folder: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
        }
    }

    public static boolean createSnapshot(Logger logger, Game game) {
        Date date = new Date();
        long time = date.getTime();
        Path snapshotPath = gameFilePath(game.getId(), Long.toString(time));
        return writeFile(logger, snapshotPath, game.serializeGame());
    }
}

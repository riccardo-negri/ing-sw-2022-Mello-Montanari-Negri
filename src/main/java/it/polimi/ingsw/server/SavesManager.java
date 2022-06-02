package it.polimi.ingsw.server;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavesManager {
    static final String SAVES_ROOT = "game_states";

    static final String USERNAMES_FILE_NAME = "usernames";

    final Logger logger;

    public SavesManager(Logger logger) {
        this.logger = logger;
    }

    public static Path gameFolderPath(String code) {
        return Paths.get(SAVES_ROOT, code);
    }

    public static Path gameFilePath(String code, String fileName) {
        return Paths.get(SAVES_ROOT, code, fileName);
    }

    boolean createFolder(Path path) {
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

    boolean touchFile(Path path) {
        return writeFile(path, "");
    }

    // replaces the file if already exists or creates it otherwise
    boolean writeFile(Path path, String text) {
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

    boolean deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            String toLog = "Unable to delete file: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
            return false;
        }
        return true;
    }

    String readFile(Path path) {
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

    public void deleteGameFolder(String code) {
        Path folderPath = gameFolderPath(code);
        try {
            FileUtils.deleteDirectory(folderPath.toFile());
        } catch (IOException e) {
            String toLog = "Unable to delete folder: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
        }
    }
}

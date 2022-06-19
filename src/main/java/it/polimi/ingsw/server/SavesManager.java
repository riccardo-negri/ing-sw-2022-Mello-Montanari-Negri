package it.polimi.ingsw.server;

import org.apache.commons.io.FileUtils;

import java.io.File;
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

    /**
     * creates a saves manager with methods to operate on files and directories
     * @param logger the eventual filesystem errors are sent to log
     */
    public SavesManager(Logger logger) {
        this.logger = logger;
    }

    /**
     * create the path object for a specific game save folder
     * @param code the code of the game folder
     * @return the path object representing the folder location
     */
    public static Path gameFolderPath(String code) {
        return Paths.get(SAVES_ROOT, code);
    }

    /**
     * create the path object for a specific game save file
     * @param code the code of the game folder
     * @param fileName the name of the single file
     * @return the path object representing the file location
     */
    public static Path gameFilePath(String code, String fileName) {
        return Paths.get(SAVES_ROOT, code, fileName);
    }

    /**
     * create a folder at the specified path
     * @param path the object representing the folder location
     * @return if the folder was created successfully
     */
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

    /**
     * replaces the file if already exists or creates it otherwise
     * @param path the object representing the file location
     * @param text the content to write in the file
     * @return if the file is written correctly
     */
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

    /**
     * delete a file
     * @param path the object representing the file location
     */
    void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            String toLog = "Unable to delete file: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
        }
    }

    /**
     * read a file and return the content as a string
     * @param path the object representing the file location
     * @return the string containing the file content
     */
    public String readFile(Path path) {
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

    /**
     * delete a geme folder and all the files inside
     * @param code the code of the game save folder
     */
    public void deleteGameFolder(String code) {
        Path folderPath = gameFolderPath(code);
        try {
            FileUtils.deleteDirectory(folderPath.toFile());
        } catch (IOException e) {
            String toLog = "Unable to delete folder: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
        }
    }

    /**
     * list all the files inside a directory
     * @param path the string containing the directory path
     * @return the array of files inside the directory
     */
    public static File[] listDirectory(String path) {
        Path folder = Paths.get(path);
        return listDirectory(folder);
    }

    /**
     * list all the files inside a directory
     * @param folder the object representing the directory location
     * @return the array of files inside the directory
     */
    public static File[] listDirectory(Path folder) {
        return folder.toFile().listFiles();
    }
}

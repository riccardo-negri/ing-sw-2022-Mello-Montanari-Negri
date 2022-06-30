package it.polimi.ingsw.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class is used as logger if the debug mode is disabled, when calling logger.log() instead of writing to a file
 * does nothing
 */
public class FakeLogger extends Logger {

    /**
     * calls the super constructor with default parameters values
     */
    protected FakeLogger() {
        super("", null);
    }

    /**
     * does nothing
     * @param level the importance level of the log record
     * @param msg the content of the log record
     */
    @Override
    public void log (Level level, String  msg) {
        // does nothing because logging is disabled
    }
}

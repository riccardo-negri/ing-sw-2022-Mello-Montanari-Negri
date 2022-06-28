package it.polimi.ingsw.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * offers methods to create a logger and to format the output correctly
 */
public class LogFormatter extends Formatter {

    /**
     * create a logger with a specific formatter and the given name
     * @param name the name of the new logger
     * @return the new logger
     */
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        FileHandler fh;
        try {
            logger.setUseParentHandlers(false);
            fh = new FileHandler("./" + name + ".log");
            logger.setLevel(Level.ALL);
            logger.addHandler(fh);
            LogFormatter formatter = new LogFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        return logger;
    }

    /**
     * format new record as a string
     * @param r contains all the info related to the new record
     * @return the string to add to the log
     */
    @Override
    public String format (LogRecord r) {
        StringBuilder log = new StringBuilder();
        final String s = " ";

        log.append(ansi().fgBrightRed());
        log.append("[");
        log.append(r.getLevel().getName());
        log.append("]");
        if(r.getLevel().getName().length() < 8) log.append(s.repeat(8 - r.getLevel().getName().length()));

        log.append(ansi().fgBrightCyan());
        log.append("[");
        log.append(calcDate(r.getMillis()));
        log.append("]");

        log.append(" ");
        if(r.getSourceClassName().length() < 50) log.append(r.getSourceClassName()).append(s.repeat(50 - r.getSourceClassName().length()));

        log.append(ansi().fgDefault());
        log.append(r.getMessage());

        Object[] params = r.getParameters();
        if (params != null) {
            log.append("\t");
            for (int i = 0; i < params.length; i++) {
                log.append(params[i]);
                if (i < params.length - 1)
                    log.append(", ");
            }
        }

        log.append(ansi().fgDefault());
        log.append("\n");
        return log.toString();
    }

    /**
     * format time as a datetime string
     * @param millisecond milliseconds since 1970 of desired event
     * @return event datetime string
     */
    private String calcDate (long millisecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        return dateFormat.format(date);
    }
}
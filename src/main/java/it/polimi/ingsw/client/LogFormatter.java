package it.polimi.ingsw.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import static org.fusesource.jansi.Ansi.ansi;

public class LogFormatter extends Formatter {
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

    private String calcDate (long millisecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        return dateFormat.format(date);
    }
}
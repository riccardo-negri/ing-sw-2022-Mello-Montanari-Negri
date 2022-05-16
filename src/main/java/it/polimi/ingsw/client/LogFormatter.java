package it.polimi.ingsw.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import static org.fusesource.jansi.Ansi.ansi;

public class LogFormatter extends Formatter {
    @Override
    public String format (LogRecord record) {
        StringBuilder log = new StringBuilder();
        String s = " ";

        log.append(ansi().fgBrightRed());
        log.append("[");
        log.append(record.getLevel().getName());
        log.append(s.repeat(9-record.getLevel().getName().length()));
        log.append("]\t");

        log.append(ansi().fgBrightCyan());
        log.append("[");
        log.append(calcDate(record.getMillis()));
        log.append("]\t");
        log.append(record.getSourceClassName());

        log.append("\t\t");

        log.append(ansi().fgDefault());
        log.append(record.getMessage());

        Object[] params = record.getParameters();

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
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        return date_format.format(date);
    }
}
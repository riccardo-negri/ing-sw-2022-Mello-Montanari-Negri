package it.polimi.ingsw.client.ui.cli.utils;

import org.fusesource.jansi.AnsiConsole;
import org.jline.builtins.Completers;
//import org.jline.console.ArgDesc;
//import org.jline.console.CmdDesc;
//import org.jline.console.Printer;
import org.jline.console.CmdDesc;
import org.jline.console.impl.JlineCommandRegistry;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.AutosuggestionWidgets;
import org.jline.widget.TailTipWidgets;
//import org.jline.widget.TailTipWidgets;

import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.IntStream;
import java.util.regex.*;

public class CoreUtilsCLI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void clearTerminal (Terminal terminal) {
        terminal.writer().println(ansi().reset().eraseScreen());
        terminal.writer().flush();
    }

    public static void printTerminalCenteredMultilineText (Terminal terminal, String s) {
        printTerminalCenteredMultilineText(terminal, s, 0);
    }

    public static void printTerminalCenteredMultilineText (Terminal terminal, String s, int expectedFollowingLines) {
        String[] arr = s.split("\n");
        List<String> list = Arrays.asList(arr);
        int startH = terminal.getHeight() / 2 - (list.size() + expectedFollowingLines) / 2;
        IntStream.range(0, list.size()).forEach(ind ->
                terminal.writer().println(
                        ansi()
                                .cursor(
                                        startH + ind,
                                        terminal.getWidth() / 2 - list.get(ind).length() / 2
                                )
                                .a(
                                        list.get(ind)
                                )
                )

        );
        terminal.writer().flush();
    }

    public static void printTerminalCenteredLine (Terminal terminal, String s) {
        printTerminalCenteredLine(terminal, s, 0);
    }

    public static void printTerminalCenteredLine (Terminal terminal, String s, int expectedInputSize) {
        terminal.writer().print(ansi().fgDefault().cursorMove(terminal.getWidth() / 2 - (s.length() + expectedInputSize) / 2, 0).a(s + " ").fgBlue());
        terminal.writer().flush();
    }

    public static void moveCursorToEnd (Terminal terminal) {
        terminal.writer().println(ansi().cursor(terminal.getHeight() - 1, 0));
        terminal.writer().flush();
    }

    public static void moveCursorToTop (Terminal terminal) {
        terminal.writer().print(ansi().cursor(0, 0));
        terminal.writer().flush();
    }

    public static void resetCursorColors (Terminal terminal) {
        terminal.writer().print(ansi().fgDefault().bgDefault());
        terminal.writer().flush();
    }

    public static void waitEnterPressed () {
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    public static Integer readNumber () {
        int num;
        try {
            num = Integer.parseInt(scanner.nextLine());
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void printTopErrorBanner (Terminal terminal, String warning) {
        terminal.writer().println(ansi().saveCursorPosition());
        terminal.writer().print(ansi().cursor(1, 0).eraseLine());
        terminal.writer().println(
                ansi().fgBrightRed().
                        cursor(
                                1,
                                terminal.getWidth() / 2 - warning.length() / 2
                        )
                        .a(
                                warning
                        )

        );
        terminal.writer().println(ansi().restoreCursorPosition());
        terminal.writer().flush();
    }

    public static String readIPAddress (Terminal terminal) {
        String IPAddress;
        String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        String REQUEST = "Please insert the IP address of the server (default is 127.0.0.1):";
        String DEFAULT = "localhost";

        printTerminalCenteredLine(terminal, REQUEST, 10);
        terminal.writer().flush();
        IPAddress = scanner.nextLine();
        if (IPAddress.length() == 0) {
            terminal.writer().print(ansi().cursorUp(1));
            terminal.writer().print(ansi().eraseLine());
            printTerminalCenteredLine(terminal, REQUEST, 10);
            terminal.writer().print(ansi().fgBlue().a(DEFAULT).cursorDownLine());
            terminal.writer().flush();
            return DEFAULT;
        }
        else if (Pattern.compile(regex).matcher(IPAddress).matches()) {
            return IPAddress;
        }
        else {
            printTopErrorBanner(terminal, "Please type a valid IP address");
            terminal.writer().print(ansi().cursorUp(2));
            terminal.writer().print(ansi().eraseLine());
            terminal.writer().flush();
            return readIPAddress(terminal);
        }
    }

    public static String readGenericString (Terminal terminal, String requestText) {
        return readGenericString(terminal, requestText, null);
    }

    public static String readGenericString (Terminal terminal, String requestText, String defaultValue) {
        String s;

        printTerminalCenteredLine(terminal, requestText, 10);
        terminal.writer().flush();
        s = scanner.nextLine();
        if (defaultValue != null && s.length() == 0) {
            terminal.writer().print(ansi().cursorUp(1));
            terminal.writer().print(ansi().eraseLine());
            printTerminalCenteredLine(terminal, requestText, 10);
            terminal.writer().print(ansi().fgBlue().a(defaultValue).cursorDownLine());
            terminal.writer().flush();
            return defaultValue;
        }
        else if (s.length() != 0) {
            return s;
        }
        else {
            printTopErrorBanner(terminal, "Please type something");
            terminal.writer().print(ansi().cursorUp(2));
            terminal.writer().print(ansi().eraseLine());
            terminal.writer().flush();
            return readGenericString(terminal, requestText, null);
        }
    }

    public static void printEmptyLine (Terminal terminal) {
        terminal.writer().println();
        terminal.writer().flush();
    }

    public static int readNumber (Terminal terminal, String requestText, int minValue, int maxValue, Integer defaultValue) {
        int num;
        printTerminalCenteredLine(terminal, requestText, 5);
        terminal.writer().flush();
        try {
            String in = scanner.nextLine();
            if (defaultValue != null && in.length() == 0) {
                terminal.writer().print(ansi().cursorUp(1));
                terminal.writer().print(ansi().eraseLine());
                printTerminalCenteredLine(terminal, requestText, 5);
                terminal.writer().print(ansi().fgBlue().a(defaultValue).cursorDownLine());
                terminal.writer().flush();
                return defaultValue;
            }
            num = Integer.parseInt(in);
            if (minValue <= num && num <= maxValue) {
                return num;
            }
            else {
                printTopErrorBanner(terminal, "Please type a valid number");
                terminal.writer().print(ansi().cursorUp(2));
                terminal.writer().print(ansi().eraseLine());
                terminal.writer().flush();
                return readNumber(terminal, requestText, minValue, maxValue, defaultValue);
            }

        } catch (NumberFormatException e) {
            printTopErrorBanner(terminal, "Please type a valid number");
            terminal.writer().print(ansi().cursorUp(2));
            terminal.writer().print(ansi().eraseLine());
            terminal.writer().flush();
            return readNumber(terminal, requestText, minValue, maxValue, defaultValue);
        }
    }

    public static boolean readBoolean (Terminal terminal, String requestText, Boolean defaultValue) {
        String read;
        printTerminalCenteredLine(terminal, requestText, 10);
        terminal.writer().flush();
        read = scanner.nextLine();
        if (defaultValue != null && read.length() == 0) {
            return defaultValue;
        }
        else if (read.equals("y")) {
            return true;
        }
        else if (read.equals("n")) {
            return false;
        }
        else {
            printTopErrorBanner(terminal, "Please type 'y' or 'n'");
            terminal.writer().print(ansi().cursorUp(2));
            terminal.writer().print(ansi().eraseLine());
            terminal.writer().flush();
            return readBoolean(terminal, requestText, defaultValue);
        }
    }


}

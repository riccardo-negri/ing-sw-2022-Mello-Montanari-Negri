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
import java.util.*;
import java.util.stream.IntStream;
import java.util.regex.*;

public class CoreUtilsCLI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void getMoveToIsland(LineReader reader) {


        Map<String, CmdDesc> tailTips = new HashMap<>();
        AutosuggestionWidgets autosuggestionWidgets = new AutosuggestionWidgets(reader);
        autosuggestionWidgets.enable();
        TailTipWidgets tailtipWidgets = new TailTipWidgets(reader, tailTips, 0, TailTipWidgets.TipType.COMPLETER);
        tailtipWidgets.enable();
        reader.getTerminal().writer().println(ansi().fgBlue().a("TEST"));
        String temp = reader.readLine(">> ");
        System.out.println(temp);
    }

    public static Integer getTerminalWidth () {
        int terminalWidth;
        try {
            terminalWidth = Integer.parseInt(String.valueOf(TerminalBuilder.terminal().getWidth()));
            return terminalWidth;
        } catch (Exception ignored) {
            return 100;
        }
    }

    public static Integer getTerminalHeight () {
        int terminalHeight;
        try {
            terminalHeight = org.jline.terminal.TerminalBuilder.terminal().getHeight();
            return terminalHeight;
        } catch (Exception ignored) {
            return 50;
        }

    }

    public static void clearTerminal () {
        AnsiConsole.out().println(ansi().reset().eraseScreen());
    }

    public static void printTerminalCenteredMultilineText (String s) {
        printTerminalCenteredMultilineText (s, 0);
    }

    public static void printTerminalCenteredMultilineText (String s, int expectedFollowingLines) {
        String[] arr = s.split("\n");
        List<String> list = Arrays.asList(arr);
        int startH = getTerminalHeight() / 2 - (list.size()+expectedFollowingLines) / 2;
        IntStream.range(0, list.size()).forEach(ind ->
                AnsiConsole.out().println(
                        ansi()
                                .cursor(
                                        startH + ind,
                                        getTerminalWidth() / 2 - list.get(ind).length() / 2
                                )
                                .a(
                                        list.get(ind)
                                )
                )
        );
    }

    public static void printTerminalCenteredMultilineText (LineReader reader, String s) {
        String[] arr = s.split("\n");
        List<String> list = Arrays.asList(arr);
        int startH = reader.getTerminal().getHeight() / 2 - (list.size()) / 2;
        //Ansi ansi = ansi();
        IntStream.range(0, list.size()).forEach(ind ->
                reader.getTerminal().writer().println(
                        ansi()
                                .cursor(
                                        startH + ind,
                                        reader.getTerminal().getWidth() / 2 - list.get(ind).length() / 2
                                )
                                .a(
                                        list.get(ind)
                                )
                )
        );
        System.out.println(ansi().reset());
    }

    public static void printTerminalCenteredLine (String s) {
        printTerminalCenteredLine(s, 0);
    }

    public static void printTerminalCenteredLine (String s, int expectedInputSize) {
        AnsiConsole.out().print(ansi().fgDefault().cursorMove(getTerminalWidth() / 2 - (s.length()+expectedInputSize) / 2, 0).a(s + " ").fgBlue());
    }

    public static void moveCursorToEnd () {
        AnsiConsole.out().println(ansi().cursor(getTerminalHeight()-1,0));
    }

    public static void moveCursorToTop () {
        AnsiConsole.out().print(ansi().cursor(0,0));
    }

    public static void resetCursorColors() {
        AnsiConsole.out().print(ansi().fgDefault().bgDefault());
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

    public static void printTopErrorBanner (String warning) {
        AnsiConsole.out().println(ansi().saveCursorPosition());
        AnsiConsole.out().print(ansi().cursor(1,0).eraseLine());
        AnsiConsole.out().println(
                ansi().fgBrightRed().
                        cursor(
                                1,
                                getTerminalWidth() / 2 - warning.length() / 2
                        )
                        .a(
                                warning
                        )

        );
        AnsiConsole.out().println(ansi().restoreCursorPosition());
    }

    public static String readIPAddress () {
        String IPAddress;
        String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        String REQUEST = "Please insert the IP address of the server (default is 127.0.0.1):";
        String DEFAULT = "localhost";

        printTerminalCenteredLine(REQUEST,10);
        IPAddress = scanner.nextLine();
        if (IPAddress.length() == 0) {
            AnsiConsole.out().print(ansi().cursorUp(1));
            AnsiConsole.out().print(ansi().eraseLine());
            printTerminalCenteredLine(REQUEST,10);
            AnsiConsole.out().print(ansi().fgBlue().a(DEFAULT).cursorDownLine());
            return DEFAULT;
        }
        else if (Pattern.compile(regex).matcher(IPAddress).matches()) {
            return IPAddress;
        }
        else {
            printTopErrorBanner("Please type a valid IP address");
            AnsiConsole.out().print(ansi().cursorUp(2));
            AnsiConsole.out().print(ansi().eraseLine());
            return readIPAddress();
        }
    }

    public static String readGenericString (String requestText) {
        return readGenericString(requestText, null);
    }

    public static String readGenericString (String requestText, String defaultValue) {
        String s;

        printTerminalCenteredLine(requestText,10);
        s = scanner.nextLine();
        if (defaultValue != null && s.length() == 0) {
            AnsiConsole.out().print(ansi().cursorUp(1));
            AnsiConsole.out().print(ansi().eraseLine());
            printTerminalCenteredLine(requestText,10);
            AnsiConsole.out().print(ansi().fgBlue().a(defaultValue).cursorDownLine());
            return defaultValue;
        }
        else if (s.length() != 0) {
            return s;
        }
        else {
            printTopErrorBanner("Please type something");
            AnsiConsole.out().print(ansi().cursorUp(2));
            AnsiConsole.out().print(ansi().eraseLine());
            return readGenericString(requestText, null);
        }
    }

    public static void printEmptyLine() {
        AnsiConsole.out().println();
    }

    public static int readNumber(String requestText, int minValue, int maxValue, Integer defaultValue) {
        int num;
        printTerminalCenteredLine(requestText,5);
        try {
            String in = scanner.nextLine();
            if (defaultValue != null && in.length() == 0) {
                AnsiConsole.out().print(ansi().cursorUp(1));
                AnsiConsole.out().print(ansi().eraseLine());
                printTerminalCenteredLine(requestText,5);
                AnsiConsole.out().print(ansi().fgBlue().a(defaultValue).cursorDownLine());
                return defaultValue;
            }
            num = Integer.parseInt(in);
            if (minValue <= num && num <= maxValue) {
                return num;
            }
            else {
                printTopErrorBanner("Please type a valid number");
                AnsiConsole.out().print(ansi().cursorUp(2));
                AnsiConsole.out().print(ansi().eraseLine());
                return readNumber(requestText, minValue, maxValue, defaultValue);
            }

        } catch (NumberFormatException e) {
            printTopErrorBanner("Please type a valid number");
            AnsiConsole.out().print(ansi().cursorUp(2));
            AnsiConsole.out().print(ansi().eraseLine());
            return readNumber(requestText, minValue, maxValue, defaultValue);
        }
    }

    public static boolean readBoolean(String requestText) {
        String read;
        printTerminalCenteredLine(requestText,10);
        read = scanner.nextLine();
        if (read.equals("y")) {
            return true;
        }
        else if (read.equals("n")) {
            return false;
        }
        else {
            printTopErrorBanner("Please type 'y' or 'n'");
            AnsiConsole.out().print(ansi().cursorUp(2));
            AnsiConsole.out().print(ansi().eraseLine());
            return readBoolean(requestText);
        }
    }


}

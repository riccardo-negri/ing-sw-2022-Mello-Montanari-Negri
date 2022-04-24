package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.regex.*;

public class CoreCLI implements UI {
    Scanner scanner = new Scanner(System.in);

    public void init () {
        AnsiConsole.systemInstall();
    }


    @Override
    public AbstractClientState getState (Client client, ClientState nextState) {
        switch (nextState) {
            case WELCOME_PAGE:
                return new WelcomePageCLI(client);
            case START_PAGE:
                return new StartPageCLI(client);
            case CONNECTION_PAGE:
                return new ConnectionPageCLI(client);
            case GAME_MODE_SELECTION_PAGE:
                return new GameModeSelectionPageCLI(client);
            case MATCHMAKING_PAGE:
                return new MatchmakingPageCLI(client);
            case BOARD_PAGE:
                return new BoardPageCLI(client);
        }
        return null;
    }


    public Integer getTerminalWidth () {
        int terminalWidth;
        try {
            terminalWidth = org.jline.terminal.TerminalBuilder.terminal().getWidth();
            return terminalWidth;
        } catch (Exception ignored) {
            return 100;
        }
    }

    public Integer getTerminalHeight () {
        int terminalHeight;
        try {
            terminalHeight = org.jline.terminal.TerminalBuilder.terminal().getHeight();
            return terminalHeight;
        } catch (Exception ignored) {
            return 50;
        }

    }

    public void clearTerminal () {
        AnsiConsole.out().println(ansi().reset().eraseScreen());
    }

    public void printTerminalCenteredMultilineText (String s) {
        printTerminalCenteredMultilineText (s, 0);
    }

    public void printTerminalCenteredMultilineText (String s, int expectedFollowingLines) {
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

    public void printTerminalCenteredLine (String s) {
        printTerminalCenteredLine(s, 0);
    }

    public void printTerminalCenteredLine (String s, int expectedInputSize) {
        AnsiConsole.out().print(ansi().fgDefault().cursorMove(getTerminalWidth() / 2 - (s.length()+expectedInputSize) / 2, 0).a(s + " ").fgBlue());
    }

    public void moveCursorToEnd () {
        AnsiConsole.out().println(ansi().cursor(getTerminalHeight()-1,0));
    }

    public void waitKeyPressed () {
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    public Integer readNumber () {
        int num;
        try {
            num = Integer.parseInt(scanner.nextLine());
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void printTopErrorBanner (String warning) {
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

    public String readIPAddress () {
        String IPAddress;
        String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        String REQUEST = "Please insert the IP address of the server (default is 127.0.0.1):";
        String DEFAULT = "127.0.0.1";

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

    public void printEmptyLine() {
        AnsiConsole.out().println();
    }

    public int readNumber(String requestText, int minValue, int maxValue, Integer defaultValue) {
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

    public boolean readBoolean(String requestText) {
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

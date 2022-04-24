package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.pages.AbstractClientState;
import it.polimi.ingsw.client.pages.ClientState;
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
        clearTerminal();
        String[] arr = s.split("\n");
        List<String> list = Arrays.asList(arr);
        int startH = getTerminalHeight() / 2 - list.size() / 2;
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

    public void bringCursorToEnd () {
        AnsiConsole.out().println(ansi().cursor(1000, 1000));
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

    public void bringCursorToMid (int lengthExpected) {
        AnsiConsole.out().print(ansi().cursorMove(getTerminalWidth() / 2 - lengthExpected / 2, 0));
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

    public int readPortNumber() {
        int portNumber;
        String REQUEST = "Please insert the port number of the server (default is 50000):";
        int DEFAULT = 50000;
        printTerminalCenteredLine(REQUEST,5);
        try {
            String in = scanner.nextLine();
            if (in.length() == 0) {
                AnsiConsole.out().print(ansi().cursorUp(1));
                AnsiConsole.out().print(ansi().eraseLine());
                printTerminalCenteredLine(REQUEST,5);
                AnsiConsole.out().print(ansi().fgBlue().a(DEFAULT).cursorDownLine());
                return DEFAULT;
            }
            portNumber = Integer.parseInt(in);
            if (49152 <= portNumber && portNumber <= 65535) {
                return portNumber;
            }
            else {
                printTopErrorBanner("Please type a valid port number");
                AnsiConsole.out().print(ansi().cursorUp(2));
                AnsiConsole.out().print(ansi().eraseLine());
                return readPortNumber();
            }

        } catch (NumberFormatException e) {
            printTopErrorBanner("Please type a valid port number");
            AnsiConsole.out().print(ansi().cursorUp(2));
            AnsiConsole.out().print(ansi().eraseLine());
            return readPortNumber();
        }
    }

    public void printEmptyLine() {
        AnsiConsole.out().println();
    }
}

package it.polimi.ingsw.client.ui.cli.utils;

import it.polimi.ingsw.networking.LobbyDescriptor;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.*;
import java.util.stream.IntStream;
import java.util.regex.*;

public class CoreUtilsCLI {
    private CoreUtilsCLI () {

    }

    public static void clearTerminal (Terminal terminal) {
        terminal.writer().println(ansi().reset().eraseScreen());
        terminal.writer().flush();
    }

    public static void moveCursorToTop (Terminal terminal) {
        terminal.writer().println(ansi().cursor(0, 0));
        terminal.writer().flush();
    }

    public static void moveCursorToEnd (Terminal terminal) {
        terminal.writer().println(ansi().cursor(terminal.getHeight() - 1, 0));
        terminal.writer().flush();
    }

    public static void waitEnterPressed (Terminal terminal) {
        final LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        try {
            reader.readLine(" ");
        } catch (Exception ignored) {
            assert true; // nop
        }
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
        terminal.writer().print(ansi().fgDefault().cursorMove(terminal.getWidth() / 2 - (s.length() + expectedInputSize) / 2, 0).a(s).fgBlue());
        terminal.writer().flush();
    }

    public static void printEmptyLine (Terminal terminal) {
        terminal.writer().println();
        terminal.writer().flush();
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

    public static void printLobby (Terminal terminal, List<Integer> base, LobbyDescriptor lobby) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);
        final String code = lobby.getCode();
        final int playersNumber = lobby.getPlayerNumber().getWizardNumber();
        final String mode = lobby.getGameMode().toString();
        final List<String> players = lobby.getConnected();

        final String R1 = "+----------------+";
        final String R2 = "|                |";

        terminal.writer().print(ansi().cursor(baseRow, baseCol).a(R1));
        terminal.writer().print(ansi().cursor(baseRow, baseCol + 7).fgRgb(255, 165, 0).a(code).fgDefault());

        terminal.writer().print(ansi().cursor(baseRow + 1, baseCol).a(R2));
        terminal.writer().print(ansi().cursor(baseRow + 1, baseCol + 2).a("Players: " + playersNumber));

        terminal.writer().print(ansi().cursor(baseRow + 2, baseCol).a(R2));
        terminal.writer().print(ansi().cursor(baseRow + 2, baseCol + 2).a("Mode: " + mode));

        terminal.writer().print(ansi().cursor(baseRow + 3, baseCol).a(R2));

        terminal.writer().print(ansi().cursor(baseRow + 4, baseCol).a(R1));
        terminal.writer().print(ansi().cursor(baseRow + 4, baseCol + 6).a("Lobby"));

        int n = 5;
        for (String p : players) {
            if (p.length() > 14) {
                p = p.substring(0, 14);
            }
            terminal.writer().println(ansi().cursor(baseRow + n, baseCol).a(R2));
            terminal.writer().println(ansi().cursor(baseRow + n, baseCol + 2).a(p));
            n++;
        }

        terminal.writer().println(ansi().cursor(baseRow + n, baseCol).a(R1));
    }

    public static Integer readNumber (Terminal terminal) {
        int num;
        final LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        try {
            num = Integer.parseInt(reader.readLine(" "));
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String readIPAddress (Terminal terminal) {
        final LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        final String zeroTo255 = "(\\d{1,2}|([01])\\" + "d{2}|2[0-4]\\d|25[0-5])";
        final String REGEX = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        final String REQUEST = "Please insert the IP address of the server (default is 127.0.0.1):";
        final String DEFAULT = "localhost";


        printTerminalCenteredLine(terminal, REQUEST, 10);
        terminal.writer().flush();
        String ipAddress = reader.readLine(" ");
        if (ipAddress.length() == 0) {
            terminal.writer().print(ansi().cursorUp(1));
            terminal.writer().print(ansi().eraseLine());
            printTerminalCenteredLine(terminal, REQUEST, 10);
            terminal.writer().print(ansi().fgBlue().a(" " + DEFAULT).cursorDownLine());
            terminal.writer().flush();
            return DEFAULT;
        }
        else if (Pattern.compile(REGEX).matcher(ipAddress).matches()) {
            return ipAddress;
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
        final LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        printTerminalCenteredLine(terminal, requestText, 10);
        terminal.writer().flush();
        s = reader.readLine(" ");
        if (defaultValue != null && s.length() == 0) {
            terminal.writer().print(ansi().cursorUp(1));
            terminal.writer().print(ansi().eraseLine());
            printTerminalCenteredLine(terminal, requestText, 10);
            terminal.writer().print(ansi().fgBlue().a(" " + defaultValue).cursorDownLine());
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

    public static String readLobbyCode (Terminal terminal, String requestText, List<LobbyDescriptor> lobbies) {
        String lobbyCode;
        while (true) {
            lobbyCode = readGenericString(terminal, requestText);
            boolean correct = false;
            for (LobbyDescriptor lobby : lobbies) {
                if (lobbyCode.equals(lobby.getCode())) {
                    correct = true;
                    break;
                }
            }
            if (correct || lobbyCode.equals("r")) break;
            printTopErrorBanner(terminal, "Insert a correct code!");
            terminal.writer().print(ansi().cursorUpLine(2));
            terminal.writer().print(ansi().eraseLine());
        }
        return lobbyCode;
    }

    public static int readNumber (Terminal terminal, String requestText, int minValue, int maxValue, Integer defaultValue) {
        int num;
        final LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        printTerminalCenteredLine(terminal, requestText, 5);
        terminal.writer().flush();
        try {
            String in = reader.readLine(" ");
            if (defaultValue != null && in.length() == 0) {
                terminal.writer().print(ansi().cursorUp(1));
                terminal.writer().print(ansi().eraseLine());
                printTerminalCenteredLine(terminal, requestText, 5);
                terminal.writer().print(ansi().fgBlue().a(" " + defaultValue).cursorDownLine());
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
        printTerminalCenteredLine(terminal, requestText, 1);
        terminal.writer().flush();
        final LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

        read = reader.readLine(" ");
        if (defaultValue != null && read.length() == 0) {
            terminal.writer().print(ansi().cursorUp(1));
            terminal.writer().print(ansi().eraseLine());
            printTerminalCenteredLine(terminal, requestText, 1);
            terminal.writer().print(ansi().fgBlue().a(defaultValue ? " y" : " n").cursorDownLine());
            terminal.writer().flush();
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

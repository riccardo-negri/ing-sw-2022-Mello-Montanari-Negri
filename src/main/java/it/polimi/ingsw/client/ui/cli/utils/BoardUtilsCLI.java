package it.polimi.ingsw.client.ui.cli.utils;

import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.text.MessageFormat;
import java.lang.String;
import java.util.stream.IntStream;

import static org.fusesource.jansi.Ansi.ansi;

public class BoardUtilsCLI {
    private static final String InfoR1 = "+----------------------------+";
    private static final String InfoR2 = "| {0}{1} |";
    private static final String InfoR3 = "|                            |";
    private static final String s = " ";
    private static final int InfoLengthToFill = 26;

    public static Integer getIslandWidth () {
        return 18;
    }

    public static Integer getIslandHeight () {
        return 9;
    }

    private static String getAnsiFromColor (String color, int value) {
        return getAnsiFromColor(color, String.valueOf(value));
    }

    private static String getAnsiFromColor (String color, String value) {
        switch (color) {
            case "RED":
                return ansi().fgRgb(255, 102, 102).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "GREEN":
                return ansi().fgRgb(0, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "PINK":
                return ansi().fgRgb(255, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "YELLOW":
                return ansi().fgRgb(255, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "BLUE":
                return ansi().fgRgb(51, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            default:
                return "";
        }
    }

    private static Ansi translateTowerColor (String tower) {
        switch (tower) {
            case "WHITE":
                return ansi().fgRgb(255, 255, 255).a("W-Tower");
            case "BLACK":
                return ansi().fgBrightBlack().a("B-Tower");
            case "GREY":
                return ansi().fgRgb(180, 180, 180).a("G-Tower");
            default:
                return ansi().a("       ");
        }
    }

    private static Ansi translateTowerNumberColor (String tower, int number) {
        switch (tower) {
            case "WHITE":
                return ansi().fgRgb(255, 255, 255).a(number + " W-Tower");
            case "BLACK":
                return ansi().fgBrightBlack().a(number + " B-Tower");
            case "GREY":
                return ansi().fgRgb(180, 180, 180).a(number + " G-Tower");
            default:
                return ansi().a("       ");
        }
    }

    public static void drawIsland (Terminal terminal, int ID, int baseRow, int baseCol, String tower, Integer yellow, Integer blue, Integer green, Integer red, Integer pink, boolean hasMotherNature) {
        final String R1 = "    _________\n";
        final String R2 = "   /         \\\n";
        final String R3 = "  /   ID-{0}   \\\n";
        final String R4 = " /             \\\n";
        final String R5 = "/   {0}   {1}   {2}   \\\n";
        final String R6 = "\\     {0}   {1}     /\n";
        final String R7 = " \\             /\n";
        final String R8 = "  \\  {0}  /\n";
        final String R9 = "   \\_________/\n";

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(R2));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(R3, (hasMotherNature ? ansi().fgRgb(255, 128, 0).a(String.format("%02d", ID)) + "" + ansi().fgDefault().bgDefault().a("") : String.format("%02d", ID)))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(R4));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(R5,
                        (red > 0 ? getAnsiFromColor("RED", red) : " "),
                        (green > 0 ? getAnsiFromColor("GREEN", green) : " "),
                        (pink > 0 ? getAnsiFromColor("PINK", pink) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(
                MessageFormat.format(R6,
                        (yellow > 0 ? getAnsiFromColor("YELLOW", yellow) : " "),
                        (blue > 0 ? getAnsiFromColor("BLUE", blue) : " ")

                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(R7));
        terminal.writer().println(ansi().cursor(baseRow + 7, baseCol).a(
                MessageFormat.format(R8, translateTowerColor(tower) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 8, baseCol).a(R9));
    }

    public static void drawCloud (Terminal terminal, int ID, int baseRow, int baseCol, Integer yellow, Integer blue, Integer green, Integer red, Integer pink) {
        final String R1 = "   .-\"-.";
        final String R2 = " /` C-{0} `\\";
        final String R3 = ";  {0}   {1}  ;";
        final String R4 = ";    {0}    ;";
        final String R5 = " \\ {0}   {1} /";
        final String R6 = "  `'-.-'`";

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(
                MessageFormat.format(R2, ID)
        ));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(R3,
                        (red > 0 ? getAnsiFromColor("RED", red) : " "),
                        (yellow > 0 ? getAnsiFromColor("YELLOW", yellow) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(R4,
                        (green > 0 ? getAnsiFromColor("GREEN", green) : " ")

                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(R5,
                        (blue > 0 ? getAnsiFromColor("BLUE", blue) : " "),
                        (pink > 0 ? getAnsiFromColor("PINK", pink) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(R6));
    }

    private static int getSchoolBoardWidth () {
        return 19;
    }

    public static int getSchoolBoardHeight () {
        return 8;
    }

    public static void drawSchoolBoard (Terminal terminal, int baseRow, int baseCol, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors) {
        final String R1 = "+-----------------+";
        final String R2 = "|    {0}    |";
        final String R3 = "|· · · · · · · · ·|";
        final String R4 = "|  {0}  {1}  {2}  {3}  {4}  |";
        final String R5 = "|  {0}  {1}  {2}  {3}  {4}  |";
        final String R6 = "|· · · · · · · · ·|";
        final String R7 = "|  {0}  {1}  {2}  {3}  {4}  |";
        final String R8 = "+-----------------+";

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(
                MessageFormat.format(R2, translateTowerNumberColor(towerColor, towerNumber) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(R3));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(R4,
                        (professors[0] ? getAnsiFromColor("GREEN", "P") : " "),
                        (professors[1] ? getAnsiFromColor("RED", "P") : " "),
                        (professors[2] ? getAnsiFromColor("YELLOW", "P") : " "),
                        (professors[3] ? getAnsiFromColor("PINK", "P") : " "),
                        (professors[4] ? getAnsiFromColor("BLUE", "P") : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(R5,
                        (diningColors[0] > 0 ? getAnsiFromColor("GREEN", diningColors[0]) : " "),
                        (diningColors[1] > 0 ? getAnsiFromColor("RED", diningColors[1]) : " "),
                        (diningColors[2] > 0 ? getAnsiFromColor("YELLOW", diningColors[2]) : " "),
                        (diningColors[3] > 0 ? getAnsiFromColor("PINK", diningColors[3]) : " "),
                        (diningColors[4] > 0 ? getAnsiFromColor("BLUE", diningColors[4]) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(R6));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(
                MessageFormat.format(R7,
                        (entranceColors[0] > 0 ? getAnsiFromColor("GREEN", entranceColors[0]) : " "),
                        (entranceColors[1] > 0 ? getAnsiFromColor("RED", entranceColors[1]) : " "),
                        (entranceColors[2] > 0 ? getAnsiFromColor("YELLOW", entranceColors[2]) : " "),
                        (entranceColors[3] > 0 ? getAnsiFromColor("PINK", entranceColors[3]) : " "),
                        (entranceColors[4] > 0 ? getAnsiFromColor("BLUE", entranceColors[4]) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 7, baseCol).a(R8));

    }

    public static void drawSinglePlayerArea (Terminal terminal, int baseRow, int baseCol, String playerName, String playedCard, int coins, String playedCharacter, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors) {
        drawSchoolBoard(terminal, baseRow, baseCol, towerColor, towerNumber, professors, diningColors, entranceColors);
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol + getSchoolBoardWidth() + 2).fgRgb(255, 128, 0).a(playerName).fgDefault());
        terminal.writer().print(ansi().cursor(baseRow + 3, baseCol + getSchoolBoardWidth() + 2).a("Assistant: " + playedCard));
        terminal.writer().print(ansi().cursor(baseRow + 4, baseCol + getSchoolBoardWidth() + 2).a("Coins: ").fgRgb(218, 165, 32).a(coins).fgDefault());
        terminal.writer().print(ansi().cursor(baseRow + 6, baseCol + getSchoolBoardWidth() + 2).a("Character: " + playedCharacter));
    }

    public static void drawGameInfoSection (Terminal terminal, int baseRow, int baseCol, String gameID, String serverIP, int serverPort, String gameMode, int playersNumber) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(InfoR2, "GameID: " + gameID, s.repeat(InfoLengthToFill - 8 - gameID.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(InfoR2, "Server IP: " + serverIP, s.repeat(InfoLengthToFill - 11 - serverIP.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(InfoR2, "Server Port: " + serverPort, s.repeat(InfoLengthToFill - 13 - Integer.toString(serverPort).length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(
                MessageFormat.format(InfoR2, "Game Mode: " + gameMode, s.repeat(InfoLengthToFill - 11 - gameMode.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(
                MessageFormat.format(InfoR2, "Players: " + playersNumber, s.repeat(InfoLengthToFill - 10))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 7, baseCol).a(InfoR1));
    }

    public static void drawGameStatusSection (Terminal terminal, int baseRow, int baseCol, int round, String currPlayer, String currPhase) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(InfoR2, "Round Number: " + round, s.repeat(InfoLengthToFill - 15))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(InfoR2, "Active Player: " + ansi().fgRgb(255, 128, 0).a(currPlayer).fgDefault(), s.repeat(InfoLengthToFill - 15 - currPlayer.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(InfoR2, "Turn Phase: " + currPhase, s.repeat(InfoLengthToFill - 12 - currPhase.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(InfoR1));
    }

    public static void drawGameCharactersSection (Terminal terminal, int baseRow, int baseCol, String[] characters, int[] charactersCost) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        IntStream.range(0, 3).forEach(i -> {
            terminal.writer().println(ansi().cursor(baseRow + 2 + (3 * i), baseCol).a(
                    MessageFormat.format(InfoR2, "Character " + (i + 1) + ": " + characters[i], s.repeat(InfoLengthToFill - 13 - characters[i].length()))
            ));
            terminal.writer().println(ansi().cursor(baseRow + 3 + (3 * i), baseCol).a(
                    MessageFormat.format(InfoR2, "Cost: " + ansi().fgRgb(218, 165, 32).a(charactersCost[i]).fgDefault(), s.repeat(InfoLengthToFill - 7))
            ));
            if (i != 2) {
                terminal.writer().println(ansi().cursor(baseRow + 4 + (3 * i), baseCol).a(InfoR3));
            }
        });
        terminal.writer().println(ansi().cursor(baseRow + 10, baseCol).a(InfoR1));
    }

    public static void drawDeckSection (Terminal terminal, int baseRow, int baseCol, int[] cards) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        IntStream.range(0, cards.length).forEach(i -> {
            terminal.writer().println(ansi().cursor(baseRow + 2 + i, baseCol).a(
                    MessageFormat.format(InfoR2, "Assistant n°" + cards[i] + " - " + (cards[i] + 1) / 2 + " steps", s.repeat(InfoLengthToFill - 22 - Integer.toString(cards[i]).length()))
            ));
        });
        terminal.writer().println(ansi().cursor(baseRow + cards.length + 1, baseCol).a(InfoR1));
    }

    public static void drawInfoSection (Terminal terminal, int baseRow, int baseCol, String gameID, String serverIP, int serverPort, String gameMode, int playersNumber, int round, String currPlayer, String currPhase, String[] characters, int[] charactersCost, int[] deck) {
        final String INFO = "Game Info";
        final String STATUS = "Game Status";
        final String CHARACTER = "Characters";
        final String DECK = "Your Deck";

        drawGameInfoSection(terminal, baseRow, baseCol, gameID, serverIP, serverPort, gameMode, playersNumber);
        terminal.writer().println(ansi().cursor(baseRow, baseCol + InfoLengthToFill / 2 - INFO.length() / 2 + 1).a(INFO));


        drawGameStatusSection(terminal, baseRow + 9, baseCol, round, currPlayer, currPhase);
        terminal.writer().println(ansi().cursor(baseRow + 9, baseCol + InfoLengthToFill / 2 - STATUS.length() / 2 + 1).a(STATUS));


        drawGameCharactersSection(terminal, baseRow + 16, baseCol, characters, charactersCost);
        terminal.writer().println(ansi().cursor(baseRow + 16, baseCol + InfoLengthToFill / 2 - CHARACTER.length() / 2 + 1).a(CHARACTER));

        drawDeckSection(terminal, baseRow + 28, baseCol, deck);
        terminal.writer().println(ansi().cursor(baseRow + 28, baseCol + InfoLengthToFill / 2 - DECK.length() / 2 + 1).a(DECK));

    }
    
    public static void drawConsoleArea(Terminal terminal, int baseRow) {
        final String CONSOLE = "Your Console";
        final String INSTRUCTIONS = "Write here your commands to interact with the game... (press TAB to get suggestions and autocompletion)";
        final String R = "+-----------------+";
        final String minus = "-";
        terminal.writer().println(ansi().cursor(baseRow,0).a(minus.repeat(terminal.getWidth())));
        terminal.writer().println(ansi().cursor(baseRow, 0).a("+").cursor(baseRow, terminal.getWidth()).a("+"));
        terminal.writer().println(ansi().cursor(baseRow, (terminal.getWidth()-CONSOLE.length()) / 2).fgRed().a(CONSOLE).fgDefault());
        terminal.writer().println(ansi().cursor(baseRow+1,(terminal.getWidth()-INSTRUCTIONS.length()) / 2).a(INSTRUCTIONS));
        terminal.writer().flush();
    }
}

package it.polimi.ingsw.client.ui.cli;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.text.MessageFormat;
import java.lang.String;
import java.util.stream.IntStream;

import static org.fusesource.jansi.Ansi.ansi;

public class BoardUtilsCLI {
    private static final String InfoR1 = "+----------------------------+";
    private static final String InfoR2 = "| {0}{1} |";
    private static final String InfoR3 = "|                            |";
    private static final String s = " ";
    private static final int lengthToFill = 26;

    public static Integer getIslandWidth () {
        return 18;
    }

    public static Integer getIslandHeight () {
        return 9;
    }

    private static String getAnsiFromColor(String color, int value) {
        return getAnsiFromColor(color, String.valueOf(value));
    }

    private static String getAnsiFromColor(String color, String value) {
        switch(color) {
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
            case "white":
                return ansi().fgRgb(255, 255, 255).a("W-Tower");
            case "black":
                return ansi().fgBrightBlack().a("B-Tower");
            case "grey":
                return ansi().fgRgb(180, 180, 180).a("G-Tower");
            default:
                return ansi().a("       ");
        }
    }

    private static Ansi translateTowerNumberColor (String tower, int number) {
        switch (tower) {
            case "white":
                return ansi().fgRgb(255, 255, 255).a(number + " W-Tower");
            case "black":
                return ansi().fgBrightBlack().a(number + " B-Tower");
            case "grey":
                return ansi().fgRgb(180, 180, 180).a(number + " G-Tower");
            default:
                return ansi().a("       ");
        }
    }

    public static void drawIsland (int ID, int startRow, int startCol, String tower, Integer yellow, Integer blue, Integer green, Integer red, Integer pink, boolean hasMotherNature) {
        final String R1 = "    _________\n";
        final String R2 = "   /         \\\n";
        final String R3 = "  /   ID-{0}   \\\n";
        final String R4 = " /             \\\n";
        final String R5 = "/   {0}   {1}   {2}   \\\n";
        final String R6 = "\\     {0}   {1}     /\n";
        final String R7 = " \\             /\n";
        final String R8 = "  \\  {0}  /\n";
        final String R9 = "   \\_________/\n";

        AnsiConsole.out().println(ansi().cursor(startRow, startCol).a(R1));
        AnsiConsole.out().println(ansi().cursor(startRow + 1, startCol).a(R2));
        AnsiConsole.out().println(ansi().cursor(startRow + 2, startCol).a(
                MessageFormat.format(R3, (hasMotherNature ? ansi().fgRgb(255, 128, 0).a(String.format("%02d", ID)) + "" + ansi().fgDefault().bgDefault().a("") : String.format("%02d", ID)))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 3, startCol).a(R4));
        AnsiConsole.out().println(ansi().cursor(startRow + 4, startCol).a(
                MessageFormat.format(R5,
                        (red > 0 ? getAnsiFromColor("RED", red) : " "),
                        (green > 0 ? getAnsiFromColor("GREEN", green) : " "),
                        (pink > 0 ? getAnsiFromColor("PINK", pink) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 5, startCol).a(
                MessageFormat.format(R6,
                        (yellow > 0 ? getAnsiFromColor("YELLOW", yellow) : " "),
                        (blue > 0 ? getAnsiFromColor("BLUE", blue) : " ")

                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 6, startCol).a(R7));
        AnsiConsole.out().println(ansi().cursor(startRow + 7, startCol).a(
                MessageFormat.format(R8, translateTowerColor(tower) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 8, startCol).a(R9));
    }

    public static void drawCloud(int ID, int startRow, int startCol, int numOfPlayer, Integer yellow, Integer blue, Integer green, Integer red, Integer pink) {
        final String R1 =   "   .-\"-.";
        final String R2 =   " /` C-{0} `\\";
        final String R3 =   ";  {0}   {1}  ;";
        final String R4 =   ";    {0}    ;";
        final String R5 = " \\ {0}   {1} /";
        final String R6 =   "  `'-.-'`";

        AnsiConsole.out().println(ansi().cursor(startRow, startCol).a(R1));
        AnsiConsole.out().println(ansi().cursor(startRow+1, startCol).a(
                MessageFormat.format(R2, ID)
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+2, startCol).a(
                MessageFormat.format(R3,
                        (red > 0 ? getAnsiFromColor("RED", red) : " "),
                        (yellow > 0 ? getAnsiFromColor("YELLOW", yellow) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+3, startCol).a(
                MessageFormat.format(R4,
                        (green > 0 ? getAnsiFromColor("GREEN", green) : " ")

                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+4, startCol).a(
                MessageFormat.format(R5,
                        (blue > 0 ? getAnsiFromColor("BLUE", blue) : " "),
                        (pink > 0 ? getAnsiFromColor("PINK", pink) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+5, startCol).a(R6));
    }

    private static int getSchoolBoardWidth() {
        return 19;
    }

    public static int getSchoolBoardHeight() {
        return 8;
    }

    public static void drawSchoolBoard(int startRow, int startCol, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors){
        final String R1 =   ".-----------------.";
        final String R2 =   "|    {0}    |";
        final String R3 =   "|· · · · · · · · ·|";
        final String R4 =   "|  {0}  {1}  {2}  {3}  {4}  |";
        final String R5 =   "|  {0}  {1}  {2}  {3}  {4}  |";
        final String R6 =   "|· · · · · · · · ·|";
        final String R7 =   "|  {0}  {1}  {2}  {3}  {4}  |";
        final String R8 =   "'-----------------'";

        AnsiConsole.out().println(ansi().cursor(startRow, startCol).a(R1));
        AnsiConsole.out().println(ansi().cursor(startRow + 1, startCol).a(
                MessageFormat.format(R2, translateTowerNumberColor(towerColor, towerNumber) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+2, startCol).a(R3));
        AnsiConsole.out().println(ansi().cursor(startRow + 3, startCol).a(
                MessageFormat.format(R4,
                        (professors[0] ? getAnsiFromColor("GREEN", "P") : " "),
                        (professors[1] ? getAnsiFromColor("RED", "P") : " "),
                        (professors[2] ? getAnsiFromColor("YELLOW", "P") : " "),
                        (professors[3] ? getAnsiFromColor("PINK", "P") : " "),
                        (professors[4] ? getAnsiFromColor("BLUE", "P") : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 4, startCol).a(
                MessageFormat.format(R5,
                        (diningColors[0] > 0 ? getAnsiFromColor("GREEN", diningColors[0]) : " "),
                        (diningColors[1] > 0 ? getAnsiFromColor("RED", diningColors[1]) : " "),
                        (diningColors[2] > 0 ? getAnsiFromColor("YELLOW", diningColors[2]) : " "),
                        (diningColors[3] > 0 ? getAnsiFromColor("PINK", diningColors[3]) : " "),
                        (diningColors[4] > 0 ? getAnsiFromColor("BLUE", diningColors[4]) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow + 5, startCol).a(R6));
        AnsiConsole.out().println(ansi().cursor(startRow + 6, startCol).a(
                MessageFormat.format(R7,
                        (entranceColors[0] > 0 ? getAnsiFromColor("GREEN", entranceColors[0]) : " "),
                        (entranceColors[1] > 0 ? getAnsiFromColor("RED", entranceColors[1]) : " "),
                        (entranceColors[2] > 0 ? getAnsiFromColor("YELLOW", entranceColors[2]) : " "),
                        (entranceColors[3] > 0 ? getAnsiFromColor("PINK", entranceColors[3]) : " "),
                        (entranceColors[4] > 0 ? getAnsiFromColor("BLUE", entranceColors[4]) : " ")
                )
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+7, startCol).a(R8));

    }

    public static void drawSinglePlayerArea(int startRow, int startCol, String playerName, String playedCard, int coins, String playedCharacter, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors) {
        drawSchoolBoard(startRow, startCol, towerColor, towerNumber, professors, diningColors, entranceColors);
        AnsiConsole.out().println(ansi().cursor(startRow+1, startCol+getSchoolBoardWidth()+2).fgRgb(255, 128 ,0).a(playerName).fgDefault());
        AnsiConsole.out().print(ansi().cursor(startRow+3, startCol+getSchoolBoardWidth()+2).a("Assistant: " + playedCard));
        AnsiConsole.out().print(ansi().cursor(startRow+4, startCol+getSchoolBoardWidth()+2).a("Coins: ").fgRgb(218, 165,32).a(coins).fgDefault());
        AnsiConsole.out().print(ansi().cursor(startRow+6, startCol+getSchoolBoardWidth()+2).a("Character: " + playedCharacter));
    }

    public static void drawGameInfoSection(int startRow, int startCol, String gameID, String serverIP, int serverPort, String gameMode, int playersNumber) {
        AnsiConsole.out().println(ansi().cursor(startRow,startCol).a(InfoR1));
        AnsiConsole.out().println(ansi().cursor(startRow+1,startCol).a(InfoR3));
        AnsiConsole.out().println(ansi().cursor(startRow+2, startCol).a(
                MessageFormat.format(InfoR2, "GameID: " + gameID, s.repeat(lengthToFill-8-gameID.length()))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+3, startCol).a(
                MessageFormat.format(InfoR2, "Server IP: " + serverIP, s.repeat(lengthToFill-11-serverIP.length()))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+4, startCol).a(
                MessageFormat.format(InfoR2, "Server Port: " + serverPort, s.repeat(lengthToFill-13-Integer.toString(serverPort).length()))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+5, startCol).a(
                MessageFormat.format(InfoR2, "Game Mode: " + gameMode, s.repeat(lengthToFill-11-gameMode.length()))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+6, startCol).a(
                MessageFormat.format(InfoR2, "Players: " + playersNumber, s.repeat(lengthToFill-10))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+7,startCol).a(InfoR1));
    }

    public static void drawGameStatusSection(int startRow, int startCol, int round, String currPlayer, String currPhase) {
        AnsiConsole.out().println(ansi().cursor(startRow,startCol).a(InfoR1));
        AnsiConsole.out().println(ansi().cursor(startRow+1,startCol).a(InfoR3));
        AnsiConsole.out().println(ansi().cursor(startRow+2, startCol).a(
                MessageFormat.format(InfoR2, "Round Number: " + round, s.repeat(lengthToFill-15))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+3, startCol).a(
                MessageFormat.format(InfoR2, "Active Player: " + ansi().fgRgb(255, 128 ,0).a(currPlayer).fgDefault(), s.repeat(lengthToFill-15-currPlayer.length()))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+4, startCol).a(
                MessageFormat.format(InfoR2, "Turn Phase: " + currPhase, s.repeat(lengthToFill-12-currPhase.length()))
        ));
        AnsiConsole.out().println(ansi().cursor(startRow+5,startCol).a(InfoR1));
    }

    public static void drawGameCharactersSection(int startRow, int startCol, String[]characters, int[]charactersCost) {
        AnsiConsole.out().println(ansi().cursor(startRow,startCol).a(InfoR1));
        AnsiConsole.out().println(ansi().cursor(startRow+1,startCol).a(InfoR3));
        IntStream.range(0, 3).forEach(i -> {
            AnsiConsole.out().println(ansi().cursor(startRow + 2 + (3 * i), startCol).a(
                    MessageFormat.format(InfoR2, "Character " + (i + 1) + ": " + characters[i], s.repeat(lengthToFill - 13 - characters[i].length()))
            ));
            AnsiConsole.out().println(ansi().cursor(startRow + 3 + (3 * i), startCol).a(
                    MessageFormat.format(InfoR2, "Cost: " + ansi().fgRgb(218, 165,32).a(charactersCost[i]).fgDefault(), s.repeat(lengthToFill - 7))
            ));
            if (i != 2) {
                AnsiConsole.out().println(ansi().cursor(startRow + 4 + (3 * i), startCol).a(InfoR3));
            }
        });
        AnsiConsole.out().println(ansi().cursor(startRow+10,startCol).a(InfoR1));
    }

    public static void drawDeckSection(int startRow, int startCol, int[] cards) {
        AnsiConsole.out().println(ansi().cursor(startRow,startCol).a(InfoR1));
        AnsiConsole.out().println(ansi().cursor(startRow+1,startCol).a(InfoR3));
        IntStream.range(0, cards.length).forEach(i -> {
            AnsiConsole.out().println(ansi().cursor(startRow + 2 + i, startCol).a(
                    MessageFormat.format(InfoR2, "Card n°" + cards[i] + " - " + (cards[i]+1)/2 + " steps", s.repeat(lengthToFill - 17 - Integer.toString(cards[i]).length()))
            ));
        });
        AnsiConsole.out().println(ansi().cursor(startRow+cards.length+1,startCol).a(InfoR1));
    }

    public static void drawInfoSection (int startRow, int startCol, String gameID, String serverIP, int serverPort, String gameMode, int playersNumber, int round, String currPlayer, String currPhase, String[]characters, int[]charactersCost) {
        final String INFO = "Game Info";
        final String STATUS = "Game Status";
        final String CHARACTER = "Characters";
        final String DECK = "Your Deck";

        drawGameInfoSection(startRow, startCol, gameID, serverIP, serverPort, gameMode, playersNumber);
        AnsiConsole.out().println(ansi().cursor(startRow, startCol+lengthToFill/2-INFO.length()/2+1).a(INFO));


        drawGameStatusSection(startRow+9, startCol, round, currPlayer, currPhase);
        AnsiConsole.out().println(ansi().cursor(startRow+9, startCol+lengthToFill/2-STATUS.length()/2+1).a(STATUS));


        drawGameCharactersSection(startRow+16, startCol, characters, charactersCost);
        AnsiConsole.out().println(ansi().cursor(startRow+16, startCol+lengthToFill/2-CHARACTER.length()/2+1).a(CHARACTER));

        drawDeckSection(startRow+28, startCol, new int[]{1,2,3,4,7,8,10});
        AnsiConsole.out().println(ansi().cursor(startRow+28, startCol+lengthToFill/2-DECK.length()/2+1).a(DECK));

    }
}

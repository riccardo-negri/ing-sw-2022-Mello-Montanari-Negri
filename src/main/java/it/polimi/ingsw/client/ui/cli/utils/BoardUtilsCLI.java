package it.polimi.ingsw.client.ui.cli.utils;

import it.polimi.ingsw.model.entity.*;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.StudentColor;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.text.MessageFormat;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.fusesource.jansi.Ansi.ansi;

public class BoardUtilsCLI {
    private static final String InfoR1 = "+----------------------------+";
    private static final String InfoR2 = "| {0}{1} |";
    private static final String InfoR4 = "| {0} |";
    private static final String InfoR3 = "|                            |";
    private static final String s = " ";
    private static final int InfoLengthToFill = 26;

    private static String getAnsiFromColor (String color, int value) {
        return getAnsiFromColor(color, String.valueOf(value));
    }

    private static String getAnsiFromColor (String color, String value) {
        return switch (color) {
            case "RED" -> ansi().fgRgb(255, 102, 102).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "GREEN" -> ansi().fgRgb(0, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "PINK" -> ansi().fgRgb(255, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "YELLOW" -> ansi().fgRgb(255, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case "BLUE" -> ansi().fgRgb(51, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            default -> "";
        };
    }

    private static boolean areIslandsConnected (Game model, int firstIsl, int secondIsl) {
        for (IslandGroup g : model.getIslandGroupList()) {
            if (2 == g.getIslandList().stream().map(Island::getId).filter(integer -> integer == firstIsl || integer == secondIsl).count()) {
                return true;
            }
        }
        return false;
    }

    private static Ansi translateTowerColor (String tower) {
        return switch (tower) {
            case "WHITE" -> ansi().fgRgb(255, 255, 255).a("W-Tower");
            case "BLACK" -> ansi().fgBrightBlack().a("B-Tower");
            case "GREY" -> ansi().fgRgb(180, 180, 180).a("G-Tower");
            default -> ansi().a("       ");
        };
    }

    private static Ansi translateTowerNumberColor (String tower, int number) {
        return switch (tower) {
            case "WHITE" -> ansi().fgRgb(255, 255, 255).a(number + " W-Tower");
            case "BLACK" -> ansi().fgBrightBlack().a(number + " B-Tower");
            case "GREY" -> ansi().fgRgb(180, 180, 180).a(number + " G-Tower");
            default -> ansi().a("       ");
        };
    }

    public static void printConsoleWarning (Terminal terminal, String s) {
        terminal.writer().println(ansi().fgRgb(255, 0, 0).a(s).fgDefault());
        terminal.writer().flush();
    }

    public static void printConsoleInfo (Terminal terminal, String s) {
        terminal.writer().print(ansi().fgBlue().a(s).fgDefault());
        terminal.writer().flush();
    }

    public static void printCharacterHelper(Terminal terminal, int characterID) {
        String help;
        help = "Character " + characterID + " description: ";
        switch (characterID) {
            case 1 -> help += CharactersDescription.CHARACTER_1;
            case 2 -> help += CharactersDescription.CHARACTER_2;
            case 3 -> help += CharactersDescription.CHARACTER_3;
            case 4 -> help += CharactersDescription.CHARACTER_4;
            case 5 -> help += CharactersDescription.CHARACTER_5;
            case 6 -> help += CharactersDescription.CHARACTER_6;
            case 7 -> help += CharactersDescription.CHARACTER_7;
            case 8 -> help += CharactersDescription.CHARACTER_8;
            case 9 -> help += CharactersDescription.CHARACTER_9;
            case 10 -> help += CharactersDescription.CHARACTER_10;
            case 11 -> help += CharactersDescription.CHARACTER_11;
            case 12 -> help += CharactersDescription.CHARACTER_12;
            default -> help = "";
        }
        help += "\n";
        terminal.writer().print(ansi().fgGreen().a(help).fgDefault());
        terminal.writer().flush();
    }

    private static int getSchoolBoardWidth () {
        return 19;
    }

    private static int getSchoolBoardHeight () {
        return 8;
    }

    private static void drawConnectionWtoE (Terminal terminal, int baseRow, int baseCol) {
        final String CONNECTION_1 = "-----";
        final String CONNECTION_2 = "---";

        terminal.writer().print(ansi().cursor(baseRow, baseCol).a(CONNECTION_1));
        terminal.writer().print(ansi().cursor(baseRow + 1, baseCol + 1).a(CONNECTION_2));
        terminal.writer().print(ansi().cursor(baseRow + 2, baseCol + 1).a(CONNECTION_2));
        terminal.writer().print(ansi().cursor(baseRow + 3, baseCol).a(CONNECTION_1));
        terminal.writer().flush();
    }

    private static void drawConnectionNWtoSE (Terminal terminal, int baseRow, int baseCol) {
        final String CONNECTION = "----";

        terminal.writer().print(ansi().cursor(baseRow, baseCol).a(CONNECTION));
        terminal.writer().print(ansi().cursor(baseRow + 1, baseCol - 1).a(CONNECTION));
        terminal.writer().print(ansi().cursor(baseRow + 2, baseCol - 2).a(CONNECTION));
        terminal.writer().flush();
    }

    private static void drawConnectionNtoS (Terminal terminal, int baseRow, int baseCol) {
        final String CONNECTION_1 = "| | |";
        final String CONNECTION_2 = "|_|_|";

        terminal.writer().print(ansi().cursor(baseRow, baseCol).a(CONNECTION_1));
        terminal.writer().print(ansi().cursor(baseRow + 1, baseCol).a(CONNECTION_2));
        terminal.writer().flush();
    }

    private static void drawConnectionSWtoNE (Terminal terminal, int baseRow, int baseCol) {
        final String CONNECTION = "----";

        terminal.writer().print(ansi().cursor(baseRow, baseCol).a(CONNECTION));
        terminal.writer().print(ansi().cursor(baseRow + 1, baseCol + 1).a(CONNECTION));
        terminal.writer().print(ansi().cursor(baseRow + 2, baseCol + 2).a(CONNECTION));
        terminal.writer().flush();
    }

    private static void drawIsland (Terminal terminal, int ID, int baseRow, int baseCol, String tower, Integer yellow, Integer blue, Integer green, Integer red, Integer pink, boolean hasMotherNature, boolean hasNoEntryTile) {
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

        if (hasNoEntryTile) {
            terminal.writer().print(ansi().cursor(baseRow + 1, baseCol + 8).fgRgb(255, 0, 0).a("Ø").fgDefault());
        }
    }

    private static void drawCloud (Terminal terminal, int ID, int baseRow, int baseCol, Integer yellow, Integer blue, Integer green, Integer red, Integer pink) {
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

    private static void drawTilesBridges (Terminal terminal, int baseRow, int baseCol, Game model) {
        HashMap<String, List<Integer>> relativePlacementOfBridgeBasedOnIDs = new HashMap<>(); // first int of list is row offset, second int is column offset
        relativePlacementOfBridgeBasedOnIDs.put("0:1", List.of(2, 50)); //SWtoNE
        relativePlacementOfBridgeBasedOnIDs.put("1:2", List.of(5, 70));
        relativePlacementOfBridgeBasedOnIDs.put("2:3", List.of(10, 87));
        relativePlacementOfBridgeBasedOnIDs.put("3:4", List.of(18, 94));
        relativePlacementOfBridgeBasedOnIDs.put("4:5", List.of(25, 85));
        relativePlacementOfBridgeBasedOnIDs.put("5:6", List.of(30, 68));
        relativePlacementOfBridgeBasedOnIDs.put("6:7", List.of(32, 50));
        relativePlacementOfBridgeBasedOnIDs.put("7:8", List.of(30, 33));
        relativePlacementOfBridgeBasedOnIDs.put("8:9", List.of(25, 16));
        relativePlacementOfBridgeBasedOnIDs.put("9:10", List.of(18, 6));
        relativePlacementOfBridgeBasedOnIDs.put("10:11", List.of(10, 14));
        relativePlacementOfBridgeBasedOnIDs.put("11:0", List.of(5, 31));

        if (areIslandsConnected(model, 0, 1)) {
            drawConnectionWtoE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("0:1").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("0:1").get(1));
        }
        if (areIslandsConnected(model, 1, 2)) {
            drawConnectionNWtoSE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("1:2").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("1:2").get(1));
        }
        if (areIslandsConnected(model, 2, 3)) {
            drawConnectionNWtoSE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("2:3").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("2:3").get(1));
        }
        if (areIslandsConnected(model, 3, 4)) {
            drawConnectionNtoS(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("3:4").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("3:4").get(1));
        }
        if (areIslandsConnected(model, 4, 5)) {
            drawConnectionSWtoNE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("4:5").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("4:5").get(1));
        }
        if (areIslandsConnected(model, 5, 6)) {
            drawConnectionSWtoNE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("5:6").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("5:6").get(1));
        }
        if (areIslandsConnected(model, 6, 7)) {
            drawConnectionWtoE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("6:7").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("6:7").get(1));
        }
        if (areIslandsConnected(model, 7, 8)) {
            drawConnectionNWtoSE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("7:8").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("7:8").get(1));
        }
        if (areIslandsConnected(model, 8, 9)) {
            drawConnectionNWtoSE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("8:9").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("8:9").get(1));
        }
        if (areIslandsConnected(model, 9, 10)) {
            drawConnectionNtoS(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("9:10").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("9:10").get(1));
        }
        if (areIslandsConnected(model, 10, 11)) {
            drawConnectionSWtoNE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("10:11").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("10:11").get(1));
        }
        if (areIslandsConnected(model, 11, 0)) {
            drawConnectionSWtoNE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get("11:0").get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get("11:0").get(1));
        }
    }

    public static void drawTilesAndClouds (Terminal terminal, int baseRow, int baseCol, Game model) {
        HashMap<Integer, List<Integer>> relativePlacementOfIslandBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        relativePlacementOfIslandBasedOnID.put(0, List.of(-1, 34));
        relativePlacementOfIslandBasedOnID.put(1, List.of(-1, 54));
        relativePlacementOfIslandBasedOnID.put(2, List.of(4, 71));
        relativePlacementOfIslandBasedOnID.put(3, List.of(9, 88));
        relativePlacementOfIslandBasedOnID.put(4, List.of(19, 88));
        relativePlacementOfIslandBasedOnID.put(5, List.of(24, 71));
        relativePlacementOfIslandBasedOnID.put(6, List.of(29, 54));
        relativePlacementOfIslandBasedOnID.put(7, List.of(29, 34));
        relativePlacementOfIslandBasedOnID.put(8, List.of(24, 17));
        relativePlacementOfIslandBasedOnID.put(9, List.of(19, 0));
        relativePlacementOfIslandBasedOnID.put(10, List.of(9, 0));
        relativePlacementOfIslandBasedOnID.put(11, List.of(4, 17));

        for (IslandGroup g : model.getIslandGroupList()) {
            for (Island isl : g.getIslandList()) {
                drawIsland(terminal,
                        isl.getId(),
                        baseRow + relativePlacementOfIslandBasedOnID.get(isl.getId()).get(0),
                        baseCol + relativePlacementOfIslandBasedOnID.get(isl.getId()).get(1),
                        g.getTower() != null ? g.getTower().toString() : "NO TOWER",
                        (int) isl.getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                        (int) isl.getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        (int) isl.getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                        (int) isl.getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                        (int) isl.getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                        model.getFistIslandGroup().equals(g),
                        isl.hasStopCard()
                );

            }
        }

        drawTilesBridges(terminal, baseRow, baseCol, model);

        HashMap<Integer, List<Integer>> relativePlacementOfCloudBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        switch (model.getPlayerNumber().getWizardNumber()) {
            case 2 -> {
                relativePlacementOfCloudBasedOnID.put(0, List.of(16, 37)); // row offset, column offset
                relativePlacementOfCloudBasedOnID.put(1, List.of(16, 57));
            }
            case 3 -> {
                relativePlacementOfCloudBasedOnID.put(0, List.of(16, 31));
                relativePlacementOfCloudBasedOnID.put(1, List.of(16, 47));
                relativePlacementOfCloudBasedOnID.put(2, List.of(16, 63));
            }
            case 4 -> {
                relativePlacementOfCloudBasedOnID.put(0, List.of(12, 37));
                relativePlacementOfCloudBasedOnID.put(1, List.of(12, 57));
                relativePlacementOfCloudBasedOnID.put(2, List.of(20, 37));
                relativePlacementOfCloudBasedOnID.put(3, List.of(20, 57));
            }

        }

        for (Cloud c : model.getCloudList()) {
            drawCloud(terminal,
                    c.getId(),
                    baseRow + relativePlacementOfCloudBasedOnID.get(c.getId()).get(0),
                    baseCol + relativePlacementOfCloudBasedOnID.get(c.getId()).get(1),
                    (int) c.getCloudContent().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                    (int) c.getCloudContent().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                    (int) c.getCloudContent().stream().filter(s -> s.getValue().equals(2)).count(), // green
                    (int) c.getCloudContent().stream().filter(s -> s.getValue().equals(3)).count(), // red
                    (int) c.getCloudContent().stream().filter(s -> s.getValue().equals(4)).count() // pink
            );
        }
    }

    private static void drawSchoolBoard (Terminal terminal, int baseRow, int baseCol, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors) {
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

    private static void drawSinglePlayerArea (Terminal terminal, int baseRow, int baseCol, String playerName, String playedCard, int coins, int playedCharacter, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors, boolean isDisconnected) {
        drawSchoolBoard(terminal, baseRow, baseCol, towerColor, towerNumber, professors, diningColors, entranceColors);
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol + getSchoolBoardWidth() + 2).fgRgb(255, 128, 0).a(playerName).fgRgb(255, 0, 0).a(isDisconnected ? " offline" : "").fgDefault());
        terminal.writer().print(ansi().cursor(baseRow + 3, baseCol + getSchoolBoardWidth() + 2).a("Assistant: " + playedCard));
        terminal.writer().print(ansi().cursor(baseRow + 4, baseCol + getSchoolBoardWidth() + 2).a("Coins: ").fgRgb(218, 165, 32).a(coins).fgDefault());
        if (playedCharacter != -1) {
            terminal.writer().print(ansi().cursor(baseRow + 6, baseCol + getSchoolBoardWidth() + 2).a("Character: " + playedCharacter));
        }
    }

    public static void drawPlayerBoards (Terminal terminal, int baseRow, int baseCol, Game model, ArrayList<String> usernames, String username, ArrayList<String> disconnectedUsernames) {
        HashMap<Integer, List<Integer>> relativePlacementOfPlayerBoardsBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        relativePlacementOfPlayerBoardsBasedOnID.put(0, List.of(0, 0));
        relativePlacementOfPlayerBoardsBasedOnID.put(1, List.of(getSchoolBoardHeight() + 2, 0));
        relativePlacementOfPlayerBoardsBasedOnID.put(2, List.of(2 * (getSchoolBoardHeight() + 2), 0));
        relativePlacementOfPlayerBoardsBasedOnID.put(3, List.of(3 * (getSchoolBoardHeight() + 2), 0));

        for (int i = 0; i < model.getPlayerNumber().getWizardNumber(); i++) {
            Wizard w = model.getWizard(i);
            drawSinglePlayerArea(terminal,
                    baseRow + relativePlacementOfPlayerBoardsBasedOnID.get(i).get(0),
                    baseCol + relativePlacementOfPlayerBoardsBasedOnID.get(i).get(1),
                    usernames.get(i),
                    w.getCardDeck().getCurrentCard() != null ? w.getCardDeck().getCurrentCard().toString() : "Not played",
                    w.getMoney(),
                    model.getGameState().getCurrentPlayer() == usernames.indexOf(username) ? (model.getGameState() instanceof ActionState ? (((ActionState) model.getGameState()).getActivatedCharacter() != null ? ((ActionState) model.getGameState()).getActivatedCharacter().getId() : -1) : -1) : -1,
                    w.getTowerColor().toString(),
                    w.getTowerNumber(),
                    new boolean[]{
                            model.getProfessor(StudentColor.GREEN).getMaster(null) != null && model.getProfessor(StudentColor.GREEN).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.RED).getMaster(null) != null && model.getProfessor(StudentColor.RED).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.YELLOW).getMaster(null) != null && model.getProfessor(StudentColor.YELLOW).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.PINK).getMaster(null) != null && model.getProfessor(StudentColor.PINK).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.BLUE).getMaster(null) != null && model.getProfessor(StudentColor.BLUE).getMaster(null).equals(w)
                    },
                    new int[]{
                            w.getDiningStudents(StudentColor.GREEN),
                            w.getDiningStudents(StudentColor.RED),
                            w.getDiningStudents(StudentColor.YELLOW),
                            w.getDiningStudents(StudentColor.PINK),
                            w.getDiningStudents(StudentColor.BLUE)
                    },
                    new int[]{
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(2)).count(), // green
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(3)).count(), // red
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                    },
                    disconnectedUsernames.contains(usernames.get(i))
            );
        }
    }

    private static void drawGameInfoSection (Terminal terminal, int baseRow, int baseCol, String serverIP, int serverPort, String gameMode, int playersNumber) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(InfoR2, "Server IP: " + serverIP, s.repeat(InfoLengthToFill - 11 - serverIP.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(InfoR2, "Server Port: " + serverPort, s.repeat(InfoLengthToFill - 13 - Integer.toString(serverPort).length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(InfoR2, "Game Mode: " + gameMode, s.repeat(InfoLengthToFill - 11 - gameMode.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(
                MessageFormat.format(InfoR2, "Players: " + playersNumber, s.repeat(InfoLengthToFill - 10))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(InfoR1));
    }

    private static void drawGameStatusSection (Terminal terminal, int baseRow, int baseCol, int round, String currPlayer, String currPhase) {
        if (currPlayer.length() > 11) {
            currPlayer = currPlayer.substring(0, 11);
        }
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

    private static void drawGameCharactersSection (Terminal terminal, int baseRow, int baseCol, int[] characters, int[] charactersCost, HashMap<Integer, int[]> characterColorsList, int[] NoEntryTilesList) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        IntStream.range(0, 3).forEach(i -> {
            terminal.writer().println(ansi().cursor(baseRow + 2 + (3 * i), baseCol).a(
                    MessageFormat.format(InfoR2, "Character " + characters[i], s.repeat(InfoLengthToFill - 10 - Integer.toString(characters[i]).length()))
            ));
            if (characterColorsList.get(i).length > 0) {
                final String COLORS = "{0} {1} {2} {3} {4}";
                int[] diningColors = characterColorsList.get(i);
                String colorsFilled = MessageFormat.format(COLORS,
                        (diningColors[0] > 0 ? getAnsiFromColor("GREEN", diningColors[0]) : " "),
                        (diningColors[1] > 0 ? getAnsiFromColor("RED", diningColors[1]) : " "),
                        (diningColors[2] > 0 ? getAnsiFromColor("YELLOW", diningColors[2]) : " "),
                        (diningColors[3] > 0 ? getAnsiFromColor("PINK", diningColors[3]) : " "),
                        (diningColors[4] > 0 ? getAnsiFromColor("BLUE", diningColors[4]) : " ")
                );
                terminal.writer().println(ansi().cursor(baseRow + 3 + (3 * i), baseCol).a(
                        MessageFormat.format(InfoR4,
                                "Cost: " +
                                        ansi().fgRgb(218, 165, 32).a(charactersCost[i]).fgDefault() +
                                        " - Studs: " +
                                        colorsFilled)
                ));
            }
            else if (NoEntryTilesList[i] >= 0) { // set to -1 if not supported by the character
                terminal.writer().println(ansi().cursor(baseRow + 3 + (3 * i), baseCol).a(
                        MessageFormat.format(InfoR4,
                                "Cost: " +
                                        ansi().fgRgb(218, 165, 32).a(charactersCost[i]).fgDefault() +
                                        " - NoEntry tiles: " +
                                        NoEntryTilesList[i]
                        )
                ));
            }
            else {
                terminal.writer().println(ansi().cursor(baseRow + 3 + (3 * i), baseCol).a(
                        MessageFormat.format(InfoR2, "Cost: " + ansi().fgRgb(218, 165, 32).a(charactersCost[i]).fgDefault(), s.repeat(InfoLengthToFill - 7))
                ));
            }

            if (i != 2) {
                terminal.writer().println(ansi().cursor(baseRow + 4 + (3 * i), baseCol).a(InfoR3));
            }
        });
        terminal.writer().println(ansi().cursor(baseRow + 10, baseCol).a(InfoR1));
    }

    private static void drawDeckSection (Terminal terminal, int baseRow, int baseCol, int[] cards) {
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(InfoR1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(InfoR3));
        IntStream.range(0, cards.length).forEach(i -> terminal.writer().println(ansi().cursor(baseRow + 2 + i, baseCol).a(
                MessageFormat.format(InfoR2, "Assistant n°" + cards[i] + " - " + (cards[i] + 1) / 2 + " steps", s.repeat(InfoLengthToFill - 22 - Integer.toString(cards[i]).length()))
        )));
        terminal.writer().println(ansi().cursor(baseRow + cards.length + 2, baseCol).a(InfoR1));
    }

    public static void drawInfoSection (Terminal terminal, int baseRow, int baseCol, String serverIP, int serverPort, String gameMode, int playersNumber, int round, String currPlayer, String currPhase, int[] characters, int[] charactersCost, int[] deck, HashMap<Integer, int[]> characterColorsList, int[] NoEntryTilesList) {
        final String INFO = "Game Info";
        final String STATUS = "Game Status";
        final String CHARACTER = "Characters";
        final String DECK = "Your Deck";

        drawGameInfoSection(terminal, baseRow, baseCol, serverIP, serverPort, gameMode, playersNumber);
        terminal.writer().println(ansi().cursor(baseRow, baseCol + InfoLengthToFill / 2 - INFO.length() / 2 + 1).a(INFO));


        drawGameStatusSection(terminal, baseRow + 8, baseCol, round, currPlayer, currPhase);
        terminal.writer().println(ansi().cursor(baseRow + 8, baseCol + InfoLengthToFill / 2 - STATUS.length() / 2 + 1).a(STATUS));


        if (characters != null) {
            drawGameCharactersSection(terminal, baseRow + 15, baseCol, characters, charactersCost, characterColorsList, NoEntryTilesList);
            terminal.writer().println(ansi().cursor(baseRow + 15, baseCol + InfoLengthToFill / 2 - CHARACTER.length() / 2 + 1).a(CHARACTER));
        }
        else {
            baseRow -= 12;
        }

        drawDeckSection(terminal, baseRow + 27, baseCol, deck);
        terminal.writer().println(ansi().cursor(baseRow + 27, baseCol + InfoLengthToFill / 2 - DECK.length() / 2 + 1).a(DECK));

    }

    public static void drawConsoleArea (Terminal terminal, int baseRow) {
        final String CONSOLE = "Your Console";
        final String INSTRUCTIONS = "Write here your commands to interact with the game... (press TAB to get suggestions and autocompletion)";
        final String minus = "-";
        terminal.writer().println(ansi().cursor(baseRow, 0).a(minus.repeat(terminal.getWidth())));
        terminal.writer().println(ansi().cursor(baseRow, 0).a("+").cursor(baseRow, terminal.getWidth()).a("+"));
        terminal.writer().println(ansi().cursor(baseRow, (terminal.getWidth() - CONSOLE.length()) / 2).fgRed().a(CONSOLE).fgDefault());
        terminal.writer().println(ansi().cursor(baseRow + 1, (terminal.getWidth() - INSTRUCTIONS.length()) / 2).a(INSTRUCTIONS));
        terminal.writer().flush();
    }
}

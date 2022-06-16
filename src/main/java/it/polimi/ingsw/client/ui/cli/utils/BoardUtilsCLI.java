package it.polimi.ingsw.client.ui.cli.utils;

import it.polimi.ingsw.model.entity.*;
import it.polimi.ingsw.model.entity.characters.*;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.StudentColor;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

import static org.fusesource.jansi.Ansi.ansi;

public class BoardUtilsCLI {
    private BoardUtilsCLI () {
    }

    private static final String GREEN = "GREEN";
    private static final String RED = "RED";
    private static final String YELLOW = "YELLOW";
    private static final String PINK = "PINK";
    private static final String BLUE = "BLUE";
    private static final String INFO_R1 = "+----------------------------+";
    private static final String INFO_R2 = "| {0}{1} |";
    private static final String INFO_R4 = "| {0} |";
    private static final String INFO_R3 = "|                            |";
    private static final String SPACE = " ";
    private static final int INFO_LENGTH_TO_FILL = 26;

    private static String getAnsiFromColor (String color, int value) {
        return getAnsiFromColor(color, String.valueOf(value));
    }

    private static String getAnsiFromColor (String color, String value) {
        return switch (color) {
            case RED -> ansi().fgRgb(255, 102, 102).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case GREEN -> ansi().fgRgb(0, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case PINK -> ansi().fgRgb(255, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case YELLOW -> ansi().fgRgb(255, 255, 0).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            case BLUE -> ansi().fgRgb(51, 153, 255).a(value) + "" + ansi().fgDefault().bgDefault().a("");
            default -> "";
        };
    }

    public static int[] getCharactersID (Game model) {
        if (model.getGameMode().equals(GameMode.COMPLETE)) {
            Character[] characters = model.getCharacters();
            return Arrays.stream(characters).mapToInt(Character::getId).toArray();
        }
        return new int[0];
    }

    private static int[] getCharactersCost (Character[] characters) {
        return Arrays.stream(characters).mapToInt(Character::getPrize).toArray();
    }

    private static HashMap<Integer, int[]> getColorsListFromCharacters (Character[] characters) {
        HashMap<Integer, int[]> hashmap = new HashMap<>();
        IntStream.range(0, characters.length).forEach(i -> {
            Character c = characters[i];
            if (c.getId() == 1) {
                hashmap.put(i, new int[]{
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        }
                );
            }
            else if (c.getId() == 7) {
                hashmap.put(i, new int[]{
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        }
                );
            }
            else if (c.getId() == 11) {
                hashmap.put(i, new int[]{
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        }
                );
            }
            else {
                hashmap.put(i, new int[0]);
            }
        });
        return hashmap;
    }

    private static int[] getNoEntryTilesFromCharacters (Character[] characters) {
        int[] array = new int[characters.length];
        IntStream.range(0, characters.length).forEach(i -> {
            Character c = characters[i];
            if (c.getId() == 5) {
                array[i] = ((CharacterFive) c).getStopNumber();
            }
            else {
                array[i] = -1;
            }
        });
        return array;
    }

    private static int getPlayedCharacter (Game model, int playerID) {
        if (model.getGameState().getCurrentPlayer() == playerID && model.getGameState() instanceof ActionState actionState && actionState.getActivatedCharacter() != null) {
            return actionState.getActivatedCharacter().getId();
        }
        return -1;
    }

    private static boolean areIslandsConnected (Game model, int firstIsl, int secondIsl) {
        for (IslandGroup g : model.getIslandGroupList()) {
            if (2 == g.getIslandList().stream().map(Island::getId).filter(integer -> integer == firstIsl || integer == secondIsl).count()) {
                return true;
            }
        }
        return false;
    }

    private static int findRoundNumber (Game model) {
        if (model.getGameState().getGameStateName().equals("PS")) {
            return 11 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
        }
        return 10 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
    }

    private static Ansi translateTowerColor (String tower) {
        return switch (tower) {
            case "WHITE" -> ansi().fgRgb(255, 255, 255).a("W-Tower");
            case "BLACK" -> ansi().fgBrightBlack().a("B-Tower");
            case "GRAY" -> ansi().fgRgb(180, 180, 180).a("G-Tower");
            default -> ansi().a("       ");
        };
    }

    private static Ansi translateTowerNumberColor (String tower, int number) {
        return switch (tower) {
            case "WHITE" -> ansi().fgRgb(255, 255, 255).a(number + " W-Tower");
            case "BLACK" -> ansi().fgBrightBlack().a(number + " B-Tower");
            case "GRAY" -> ansi().fgRgb(180, 180, 180).a(number + " G-Tower");
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

    public static void printCharacterHelper (Terminal terminal, int characterID) {
        String help = "Character " + characterID + " description: " + switch (characterID) {
            case 1 -> CharactersDescription.CHARACTER_1;
            case 2 -> CharactersDescription.CHARACTER_2;
            case 3 -> CharactersDescription.CHARACTER_3;
            case 4 -> CharactersDescription.CHARACTER_4;
            case 5 -> CharactersDescription.CHARACTER_5;
            case 6 -> CharactersDescription.CHARACTER_6;
            case 7 -> CharactersDescription.CHARACTER_7;
            case 8 -> CharactersDescription.CHARACTER_8;
            case 9 -> CharactersDescription.CHARACTER_9;
            case 10 -> CharactersDescription.CHARACTER_10;
            case 11 -> CharactersDescription.CHARACTER_11;
            case 12 -> CharactersDescription.CHARACTER_12;
            default -> "";
        } + "\n";
        terminal.writer().print(ansi().fgGreen().a(help).fgDefault());
        terminal.writer().flush();
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

        IntStream.range(0, 2).forEach(i -> terminal.writer().print(ansi().cursor(baseRow + i, baseCol - i).a(CONNECTION)));
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

        IntStream.range(0, 2).forEach(i -> terminal.writer().print(ansi().cursor(baseRow + i, baseCol + i).a(CONNECTION)));
        terminal.writer().flush();
    }

    private static void drawIsland (Terminal terminal, List<Integer> base, IslandGroup group, Island island, boolean hasMotherNature) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);
        final int id = island.getId();
        final String tower = group.getTower() != null ? group.getTower().toString() : "NO TOWER";
        final int yellow = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count();
        final int blue = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count();
        final int green = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count();
        final int red = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count();
        final int pink = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count();
        final boolean hasNoEntryTile = island.hasStopCard();

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
                MessageFormat.format(R3, String.format("%02d", id)))
        );
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(R4));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(R5,
                        (red > 0 ? getAnsiFromColor(RED, red) : " "),
                        (green > 0 ? getAnsiFromColor(GREEN, green) : " "),
                        (pink > 0 ? getAnsiFromColor(PINK, pink) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(
                MessageFormat.format(R6,
                        (yellow > 0 ? getAnsiFromColor(YELLOW, yellow) : " "),
                        (blue > 0 ? getAnsiFromColor(BLUE, blue) : " ")

                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(R7));
        terminal.writer().println(ansi().cursor(baseRow + 7, baseCol).a(
                MessageFormat.format(R8, translateTowerColor(tower) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 8, baseCol).a(R9));

        if (hasMotherNature) {
            final String R1_M = "_________\n";
            final String R2_M = "/";
            final String R3_M = "\\";
            final String R4_M = "\\_________/\n";

            terminal.writer().println(ansi().fgRgb(255, 128, 0).cursor(baseRow, baseCol + 4).a(R1_M));
            terminal.writer().println(ansi().cursor(baseRow + 1, baseCol + 3).a(R2_M).cursor(baseRow + 1, baseCol + 13).a(R3_M));
            terminal.writer().println(ansi().cursor(baseRow + 2, baseCol + 2).a(R2_M).cursor(baseRow + 2, baseCol + 14).a(R3_M));
            terminal.writer().println(ansi().cursor(baseRow + 3, baseCol + 1).a(R2_M).cursor(baseRow + 3, baseCol + 15).a(R3_M));
            terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(R2_M).cursor(baseRow + 4, baseCol + 16).a(R3_M));
            terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(R3_M).cursor(baseRow + 5, baseCol + 16).a(R2_M));
            terminal.writer().println(ansi().cursor(baseRow + 6, baseCol + 1).a(R3_M).cursor(baseRow + 6, baseCol + 15).a(R2_M));
            terminal.writer().println(ansi().cursor(baseRow + 7, baseCol + 2).a(R3_M).cursor(baseRow + 7, baseCol + 14).a(R2_M));
            terminal.writer().println(ansi().cursor(baseRow + 8, baseCol + 3).a(R4_M).fgDefault());
        }
        if (hasNoEntryTile) {
            terminal.writer().print(ansi().cursor(baseRow + 1, baseCol + 8).fgRgb(255, 0, 0).a("Ø").fgDefault());
        }
    }

    private static void drawCloud (Terminal terminal, List<Integer> base, Cloud cloud) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);
        final int id = cloud.getId();
        final int yellow = (int) cloud.getCloudContent().stream().filter(s -> s.getValue().equals(0)).count();
        final int blue = (int) cloud.getCloudContent().stream().filter(s -> s.getValue().equals(1)).count();
        final int green = (int) cloud.getCloudContent().stream().filter(s -> s.getValue().equals(2)).count();
        final int red = (int) cloud.getCloudContent().stream().filter(s -> s.getValue().equals(3)).count();
        final int pink = (int) cloud.getCloudContent().stream().filter(s -> s.getValue().equals(4)).count();

        final String R1 = "   .-\"-.";
        final String R2 = " /` C-{0} `\\";
        final String R3 = ";  {0}   {1}  ;";
        final String R4 = ";    {0}    ;";
        final String R5 = " \\ {0}   {1} /";
        final String R6 = "  `'-.-'`";

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(
                MessageFormat.format(R2, id)
        ));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(R3,
                        (red > 0 ? getAnsiFromColor(RED, red) : " "),
                        (yellow > 0 ? getAnsiFromColor(YELLOW, yellow) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(R4,
                        (green > 0 ? getAnsiFromColor(GREEN, green) : " ")

                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(R5,
                        (blue > 0 ? getAnsiFromColor(BLUE, blue) : " "),
                        (pink > 0 ? getAnsiFromColor(PINK, pink) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(R6));
    }

    private static void drawTilesBridges (Terminal terminal, List<Integer> base, Game model) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

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

        IntStream.range(0, 12).forEach(i -> {
            int j = i == 11 ? 0 : i + 1;
            if (areIslandsConnected(model, i, j)) {
                switch (i) {
                    case 0, 6 ->
                            drawConnectionWtoE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(1));
                    case 1, 2, 7, 8 ->
                            drawConnectionNWtoSE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(1));
                    case 3, 9 ->
                            drawConnectionNtoS(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(1));
                    default -> // 4, 5, 10, 11
                            drawConnectionSWtoNE(terminal, baseRow + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(0), baseCol + relativePlacementOfBridgeBasedOnIDs.get(i + ":" + j).get(1));
                }
            }
        });
    }

    public static void drawTilesAndClouds (Terminal terminal, List<Integer> base, Game model) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

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
                drawIsland(
                        terminal,
                        new Vector<>(Arrays.asList(baseRow + relativePlacementOfIslandBasedOnID.get(isl.getId()).get(0), baseCol + relativePlacementOfIslandBasedOnID.get(isl.getId()).get(1))),
                        g,
                        isl,
                        model.getFistIslandGroup().equals(g)
                );

            }
        }

        drawTilesBridges(terminal, base, model);

        HashMap<Integer, List<Integer>> relativePlacementOfCloudBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        switch (model.getPlayerNumber().getWizardNumber()) {
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
            default -> {
                relativePlacementOfCloudBasedOnID.put(0, List.of(16, 37)); // row offset, column offset
                relativePlacementOfCloudBasedOnID.put(1, List.of(16, 57));
            }
        }

        for (Cloud c : model.getCloudList()) {
            drawCloud(terminal,
                    new Vector<>(Arrays.asList(baseRow + relativePlacementOfCloudBasedOnID.get(c.getId()).get(0), baseCol + relativePlacementOfCloudBasedOnID.get(c.getId()).get(1))),
                    c
            );
        }
    }

    private static void drawSchoolBoard (Terminal terminal, List<Integer> base, String towerColor, int towerNumber, boolean[] professors, int[] diningColors, int[] entranceColors) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

        final String R1 = "+-----------------+";
        final String R2 = "|    {0}    |";
        final String R3 = "|· · · · · · · · ·|";
        final String R4 = "|  {0}  {1}  {2}  {3}  {4}  |";

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(
                MessageFormat.format(R2, translateTowerNumberColor(towerColor, towerNumber) + "" + ansi().fgDefault().bgDefault().a(""))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(R3));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(R4,
                        (professors[0] ? getAnsiFromColor(GREEN, "P") : " "),
                        (professors[1] ? getAnsiFromColor(RED, "P") : " "),
                        (professors[2] ? getAnsiFromColor(YELLOW, "P") : " "),
                        (professors[3] ? getAnsiFromColor(PINK, "P") : " "),
                        (professors[4] ? getAnsiFromColor(BLUE, "P") : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(R4,
                        (diningColors[0] > 0 ? getAnsiFromColor(GREEN, diningColors[0]) : " "),
                        (diningColors[1] > 0 ? getAnsiFromColor(RED, diningColors[1]) : " "),
                        (diningColors[2] > 0 ? getAnsiFromColor(YELLOW, diningColors[2]) : " "),
                        (diningColors[3] > 0 ? getAnsiFromColor(PINK, diningColors[3]) : " "),
                        (diningColors[4] > 0 ? getAnsiFromColor(BLUE, diningColors[4]) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(R3));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(
                MessageFormat.format(R4,
                        (entranceColors[0] > 0 ? getAnsiFromColor(GREEN, entranceColors[0]) : " "),
                        (entranceColors[1] > 0 ? getAnsiFromColor(RED, entranceColors[1]) : " "),
                        (entranceColors[2] > 0 ? getAnsiFromColor(YELLOW, entranceColors[2]) : " "),
                        (entranceColors[3] > 0 ? getAnsiFromColor(PINK, entranceColors[3]) : " "),
                        (entranceColors[4] > 0 ? getAnsiFromColor(BLUE, entranceColors[4]) : " ")
                )
        ));
        terminal.writer().println(ansi().cursor(baseRow + 7, baseCol).a(R1));

    }

    private static void drawSinglePlayerArea (Terminal terminal, List<Integer> base, int playerID, String playerUsername, boolean isDisconnected, Game model) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);
        final Wizard wizard = model.getWizard(playerID);
        final String playedCard = wizard.getCardDeck().getCurrentCard() != null ? wizard.getCardDeck().getCurrentCard().toString() : "Not played";
        final int coins = wizard.getMoney();
        final int playedCharacter = getPlayedCharacter(model, playerID);
        final String towerColor = wizard.getTowerColor().toString();
        final int towerNumber = wizard.getTowerNumber();
        boolean[] professors = new boolean[]{
                model.getProfessor(StudentColor.GREEN).getMaster(null) != null && model.getProfessor(StudentColor.GREEN).getMaster(null).equals(wizard),
                model.getProfessor(StudentColor.RED).getMaster(null) != null && model.getProfessor(StudentColor.RED).getMaster(null).equals(wizard),
                model.getProfessor(StudentColor.YELLOW).getMaster(null) != null && model.getProfessor(StudentColor.YELLOW).getMaster(null).equals(wizard),
                model.getProfessor(StudentColor.PINK).getMaster(null) != null && model.getProfessor(StudentColor.PINK).getMaster(null).equals(wizard),
                model.getProfessor(StudentColor.BLUE).getMaster(null) != null && model.getProfessor(StudentColor.BLUE).getMaster(null).equals(wizard)
        };
        int[] diningColors = new int[]{
                wizard.getDiningStudents(StudentColor.GREEN),
                wizard.getDiningStudents(StudentColor.RED),
                wizard.getDiningStudents(StudentColor.YELLOW),
                wizard.getDiningStudents(StudentColor.PINK),
                wizard.getDiningStudents(StudentColor.BLUE)
        };
        int[] entranceColors = new int[]{
                (int) wizard.getEntranceStudents().stream().filter(s -> s.getValue().equals(2)).count(), // green
                (int) wizard.getEntranceStudents().stream().filter(s -> s.getValue().equals(3)).count(), // red
                (int) wizard.getEntranceStudents().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                (int) wizard.getEntranceStudents().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                (int) wizard.getEntranceStudents().stream().filter(s -> s.getValue().equals(1)).count(), // blue
        };

        drawSchoolBoard(terminal, base, towerColor, towerNumber, professors, diningColors, entranceColors);
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol + 21).fgRgb(255, 128, 0).a(playerUsername).fgRgb(255, 0, 0).a(isDisconnected ? " offline" : "").fgDefault());
        terminal.writer().print(ansi().cursor(baseRow + 3, baseCol + 21).a("Assistant: " + playedCard));
        terminal.writer().print(ansi().cursor(baseRow + 4, baseCol + 21).a("Coins: ").fgRgb(218, 165, 32).a(coins).fgDefault());
        if (playedCharacter != -1) {
            terminal.writer().print(ansi().cursor(baseRow + 6, baseCol + 21).a("Character: " + playedCharacter));
        }
    }

    public static void drawPlayerBoards (Terminal terminal, List<Integer> base, Game model, List<String> usernames, List<String> disconnectedUsernames) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

        HashMap<Integer, List<Integer>> relativePlacementOfPlayerBoardsBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        relativePlacementOfPlayerBoardsBasedOnID.put(0, List.of(0, 0));
        relativePlacementOfPlayerBoardsBasedOnID.put(1, List.of(10, 0));
        relativePlacementOfPlayerBoardsBasedOnID.put(2, List.of(2 * (10), 0));
        relativePlacementOfPlayerBoardsBasedOnID.put(3, List.of(3 * (10), 0));

        IntStream.range(0, model.getPlayerNumber().getWizardNumber()).forEach(playerID -> drawSinglePlayerArea(
                terminal,
                new Vector<>(Arrays.asList(baseRow + relativePlacementOfPlayerBoardsBasedOnID.get(playerID).get(0), baseCol + relativePlacementOfPlayerBoardsBasedOnID.get(playerID).get(1))),
                playerID,
                usernames.get(playerID),
                disconnectedUsernames.contains(usernames.get(playerID)),
                model
        ));
    }

    private static void drawGameInfoSection (Terminal terminal, List<Integer> base, String serverIP, int serverPort, String gameMode, int playersNumber) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(INFO_R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(INFO_R3));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(INFO_R2, "Server IP: " + serverIP, SPACE.repeat(INFO_LENGTH_TO_FILL - 11 - serverIP.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(INFO_R2, "Server Port: " + serverPort, SPACE.repeat(INFO_LENGTH_TO_FILL - 13 - Integer.toString(serverPort).length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(INFO_R2, "Game Mode: " + gameMode, SPACE.repeat(INFO_LENGTH_TO_FILL - 11 - gameMode.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(
                MessageFormat.format(INFO_R2, "Players: " + playersNumber, SPACE.repeat(INFO_LENGTH_TO_FILL - 10))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 6, baseCol).a(INFO_R1));
    }

    private static void drawGameStatusSection (Terminal terminal, List<Integer> base, int round, String currPlayer, String currPhase) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

        if (currPlayer.length() > 11) currPlayer = currPlayer.substring(0, 11);
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(INFO_R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(INFO_R3));
        terminal.writer().println(ansi().cursor(baseRow + 2, baseCol).a(
                MessageFormat.format(INFO_R2, "Round Number: " + round, SPACE.repeat(INFO_LENGTH_TO_FILL - 15))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 3, baseCol).a(
                MessageFormat.format(INFO_R2, "Active Player: " + ansi().fgRgb(255, 128, 0).a(currPlayer).fgDefault(), SPACE.repeat(INFO_LENGTH_TO_FILL - 15 - currPlayer.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 4, baseCol).a(
                MessageFormat.format(INFO_R2, "Turn Phase: " + currPhase, SPACE.repeat(INFO_LENGTH_TO_FILL - 12 - currPhase.length()))
        ));
        terminal.writer().println(ansi().cursor(baseRow + 5, baseCol).a(INFO_R1));
    }

    private static void drawCharacterWithColors (Terminal terminal, List<Integer> base, int characterCost, int[] characterColorList) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

        final String COLORS = "{0} {1} {2} {3} {4}";

        String colorsFilled = MessageFormat.format(COLORS,
                (characterColorList[0] > 0 ? getAnsiFromColor(GREEN, characterColorList[0]) : " "),
                (characterColorList[1] > 0 ? getAnsiFromColor(RED, characterColorList[1]) : " "),
                (characterColorList[2] > 0 ? getAnsiFromColor(YELLOW, characterColorList[2]) : " "),
                (characterColorList[3] > 0 ? getAnsiFromColor(PINK, characterColorList[3]) : " "),
                (characterColorList[4] > 0 ? getAnsiFromColor(BLUE, characterColorList[4]) : " ")
        );
        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(
                MessageFormat.format(INFO_R4,
                        "Cost: " +
                                ansi().fgRgb(218, 165, 32).a(characterCost).fgDefault() +
                                " - Studs: " +
                                colorsFilled)
        ));
    }

    private static void drawGameCharactersSection (Terminal terminal, List<Integer> base, Character[] characters) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);
        final int[] charactersID = Arrays.stream(characters).mapToInt(Character::getId).toArray();
        final int[] charactersCost = getCharactersCost(characters);
        final Map<Integer, int[]> characterColorsList = getColorsListFromCharacters(characters);
        final int[] noEntryTilesList = getNoEntryTilesFromCharacters(characters);

        final String COST = "Cost: ";

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(INFO_R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(INFO_R3));
        IntStream.range(0, 3).forEach(i -> {
            terminal.writer().println(ansi().cursor(baseRow + 2 + (3 * i), baseCol).a(
                    MessageFormat.format(INFO_R2, "Character " + charactersID[i], SPACE.repeat(INFO_LENGTH_TO_FILL - 10 - Integer.toString(charactersID[i]).length()))
            ));
            if (characterColorsList.get(i).length > 0) {
                drawCharacterWithColors(terminal, new ArrayList<>(Arrays.asList(baseRow + 3 + (3 * i), baseCol)), charactersCost[i], characterColorsList.get(i));
            }
            else if (noEntryTilesList[i] >= 0) { // set to -1 if not supported by the character
                terminal.writer().println(ansi().cursor(baseRow + 3 + (3 * i), baseCol).a(
                        MessageFormat.format(INFO_R4,
                                COST +
                                        ansi().fgRgb(218, 165, 32).a(charactersCost[i]).fgDefault() +
                                        " - NoEntry tiles: " +
                                        noEntryTilesList[i]
                        )
                ));
            }
            else {
                terminal.writer().println(ansi().cursor(baseRow + 3 + (3 * i), baseCol).a(
                        MessageFormat.format(INFO_R2, COST + ansi().fgRgb(218, 165, 32).a(charactersCost[i]).fgDefault(), SPACE.repeat(INFO_LENGTH_TO_FILL - 7))
                ));
            }

            if (i != 2) {
                terminal.writer().println(ansi().cursor(baseRow + 4 + (3 * i), baseCol).a(INFO_R3));
            }
        });
        terminal.writer().println(ansi().cursor(baseRow + 10, baseCol).a(INFO_R1));
    }

    private static void drawDeckSection (Terminal terminal, List<Integer> base, int[] cards) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);

        terminal.writer().println(ansi().cursor(baseRow, baseCol).a(INFO_R1));
        terminal.writer().println(ansi().cursor(baseRow + 1, baseCol).a(INFO_R3));
        IntStream.range(0, cards.length).forEach(i -> terminal.writer().println(ansi().cursor(baseRow + 2 + i, baseCol).a(
                MessageFormat.format(INFO_R2, "Assistant n°" + cards[i] + " - " + (cards[i] + 1) / 2 + " steps", SPACE.repeat(INFO_LENGTH_TO_FILL - 22 - Integer.toString(cards[i]).length()))
        )));
        terminal.writer().println(ansi().cursor(baseRow + cards.length + 2, baseCol).a(INFO_R1));
    }

    public static void drawInfoSection (Terminal terminal, List<Integer> base, String serverIP, int serverPort, String username, List<String> usernames, Game model) {
        final int baseRow = base.get(0);
        final int baseCol = base.get(1);
        final String currPhase = model.getGameState().getGameStateName();
        final int playersNumber = model.getPlayerNumber().getWizardNumber();
        final int round = findRoundNumber(model);
        final String currPlayer = usernames.get(model.getGameState().getCurrentPlayer());
        final String gameMode = model.getGameMode().toString();
        final int[] characters = model.getGameMode().equals(GameMode.COMPLETE) ? getCharactersID(model) : null;
        final int[] deck = model.getWizard(usernames.indexOf(username)).getCardDeck().getDeckCards();

        final String INFO = "Game Info";
        final String STATUS = "Game Status";
        final String CHARACTER = "Characters";
        final String DECK = "Your Deck";

        drawGameInfoSection(terminal, new ArrayList<>(Arrays.asList(baseRow, baseCol)), serverIP, serverPort, gameMode, playersNumber);
        terminal.writer().println(ansi().cursor(baseRow, baseCol + INFO_LENGTH_TO_FILL / 2 - INFO.length() / 2 + 1).a(INFO));


        drawGameStatusSection(terminal, new ArrayList<>(Arrays.asList(baseRow + 8, baseCol)), round, currPlayer, currPhase);
        terminal.writer().println(ansi().cursor(baseRow + 8, baseCol + INFO_LENGTH_TO_FILL / 2 - STATUS.length() / 2 + 1).a(STATUS));


        if (characters != null) {
            drawGameCharactersSection(terminal, new ArrayList<>(Arrays.asList(baseRow + 15, baseCol)), model.getCharacters());
            terminal.writer().println(ansi().cursor(baseRow + 15, baseCol + INFO_LENGTH_TO_FILL / 2 - CHARACTER.length() / 2 + 1).a(CHARACTER));
            drawDeckSection(terminal, new ArrayList<>(Arrays.asList(baseRow + 27, baseCol)), deck);
            terminal.writer().println(ansi().cursor(baseRow + 27, baseCol + INFO_LENGTH_TO_FILL / 2 - DECK.length() / 2 + 1).a(DECK));
        }
        else {
            drawDeckSection(terminal, new ArrayList<>(Arrays.asList(baseRow + 15, baseCol)), deck);
            terminal.writer().println(ansi().cursor(baseRow + 15, baseCol + INFO_LENGTH_TO_FILL / 2 - DECK.length() / 2 + 1).a(DECK));
        }

    }

    public static void drawConsoleArea (Terminal terminal, int baseRow) {
        final String CONSOLE = "Your Console";
        final String INSTRUCTIONS = "Write here your commands to interact with the game... (press TAB to get suggestions and autocompletion, to resign press CTRL+C)";
        final String minus = "-";

        terminal.writer().println(ansi().cursor(baseRow, 0).a(minus.repeat(terminal.getWidth())));
        terminal.writer().println(ansi().cursor(baseRow, 0).a("+").cursor(baseRow, terminal.getWidth()).a("+"));
        terminal.writer().println(ansi().cursor(baseRow, (terminal.getWidth() - CONSOLE.length()) / 2).fgRed().a(CONSOLE).fgDefault());
        terminal.writer().println(ansi().cursor(baseRow + 1, (terminal.getWidth() - INSTRUCTIONS.length()) / 2).a(INSTRUCTIONS));
        terminal.writer().flush();
    }
}

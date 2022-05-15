package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.model.entity.*;
import it.polimi.ingsw.model.entity.characters.*;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.utils.Connection;
import it.polimi.ingsw.utils.Disconnected;
import it.polimi.ingsw.utils.Message;
import it.polimi.ingsw.utils.moves.Move;
import org.jline.reader.UserInterruptException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.client.ui.cli.utils.BoardUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.ReadMoveUtilsCLI.*;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageCLI extends AbstractBoardPage {

    volatile List<String> moveFromStdin = new ArrayList<>();

    public BoardPageCLI (Client client) {
        super(client);
    }

    /**
     * In short, here is all the controller of the CLI
     */
    @Override
    public void draw (Client client) {

        while (!client.getModel().isGameEnded()) { //TODO refactor
            drawGameBoard(client.getUsernames(), client.getUsername(), client.getIPAddress(), client.getPort());
            drawConsoleArea(terminal, 48);
            LOGGER.log(Level.FINE, "Drew game board and console area");

            if (model.getGameState().getCurrentPlayer() == client.getUsernames().indexOf(client.getUsername())) { // enter if it's your turn

                moveFromStdin = new ArrayList<>();
                waitForMoveOrMessage();

                if (moveFromStdin.size() != 0) { // a move has have been read
                    LOGGER.log(Level.FINE, "Processing input move: " + moveFromStdin + " Game state: " + model.getGameState().getGameStateName());
                    switch (model.getGameState().getGameStateName()) {

                        case "PS" -> {
                            try {
                                doCardChoice(Integer.parseInt(moveFromStdin.get(0).split(" ")[1]));
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Got an invalid move (that passed regex check), asking for it again");
                                printConsoleWarning(terminal, "Please type a valid command..." + e.toString());
                            }
                        }

                        case "MSS" -> {
                            try {
                                if (!moveFromStdin.get(0).contains("character")) {
                                    doStudentMovement(getStudentColorFromString(moveFromStdin.get(0).split(" ")[1]), moveFromStdin.get(0).split(" ")[3]);
                                }
                                else {
                                    parseAndDoCharacterMove(moveFromStdin.get(0));
                                }
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Got an invalid move (that passed regex check), asking for it again. Exception: " + e.toString());
                                printConsoleWarning(terminal, "Please type a valid command..." + e.toString());
                            }
                        }

                        case "MMNS" -> {
                            try {
                                if (!moveFromStdin.get(0).contains("character")) {
                                    doMotherNatureMovement(Integer.parseInt(moveFromStdin.get(0).split(" ")[2]));
                                }
                                else {
                                    parseAndDoCharacterMove(moveFromStdin.get(0));
                                }
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Got an invalid move (that passed regex check), asking for it again. Exception: " + e.toString());
                                printConsoleWarning(terminal, "Please type a valid command..." + e.toString());
                            }
                        }

                        case "CCS" -> {
                            try {
                                if (!moveFromStdin.get(0).contains("character")) {
                                    doCloudChoice(Integer.parseInt(moveFromStdin.get(0).split(" ")[1]));
                                }
                                else {
                                    parseAndDoCharacterMove(moveFromStdin.get(0));
                                }
                            } catch (Exception e) {
                                LOGGER.log(Level.WARNING, "Got an invalid move (that passed regex check), asking for it again. Exception: " + e.toString());
                                printConsoleWarning(terminal, "Please type a valid command..." + e.toString());
                            }
                        }
                    }
                }
                else { // no move was made, so it's a message about some disconnection events
                    handleMessageNotMove();
                }
            }
            else { // enter here if it's not your turn
                printConsoleInfo(terminal, "Waiting for the other players to do a move...");

                Message message = client.getConnection().waitMessage();

                if (message instanceof Disconnected) { //TODO support this
                    printConsoleWarning(terminal, "Received an unsupported message...");
                }
                else if (message instanceof Move) {
                    try {
                        applyOtherPlayersMove((Move) message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    printConsoleWarning(terminal, "Received an unsupported message...");
                }

            }

        }

        LOGGER.log(Level.FINE, "Game finished because someone won or surrendered");
        onEnd();

    }

    private void parseAndDoCharacterMove (String move) throws Exception {
        int ID = Integer.parseInt(move.split(" ")[0].split("-")[2]);
        ;
        ArrayList<Object> parameters = new ArrayList<>();
        switch (move.split(" ")[0]) { // adding parameters only where needed
            case "use-character-1" -> {
                parameters.add(getStudentColorFromString(move.split(" ")[2]));
                parameters.add(Integer.parseInt(move.split(" ")[4].split("-")[1]));
            }
            case "use-character-3", "use-character-5" -> {
                parameters.add(Integer.parseInt(move.split(" ")[2].split("-")[1]));
            }
            case "use-character-7" -> {
                List<StudentColor> temp1 = new ArrayList<>();
                List<StudentColor> temp2 = new ArrayList<>();

                temp1.add(getStudentColorFromString(move.split(" ")[2]));
                if (!move.split(" ")[3].contains("nothing")) {
                    temp1.add(getStudentColorFromString(move.split(" ")[3]));
                }
                if (!move.split(" ")[4].contains("nothing")) {
                    temp1.add(getStudentColorFromString(move.split(" ")[4]));
                }
                parameters.add(temp1);

                temp2.add(getStudentColorFromString(move.split(" ")[6]));
                if (!move.split(" ")[7].contains("nothing")) {
                    temp2.add(getStudentColorFromString(move.split(" ")[7]));
                }
                if (!move.split(" ")[8].contains("nothing")) {
                    temp2.add(getStudentColorFromString(move.split(" ")[8]));
                }
                parameters.add(temp2);
            }
            case "use-character-9", "use-character-11", "use-character-12" -> {
                parameters.add(getStudentColorFromString(move.split(" ")[2]));
            }
            case "use-character-10" -> {
                List<StudentColor> temp1 = new ArrayList<>();
                List<StudentColor> temp2 = new ArrayList<>();

                temp1.add(getStudentColorFromString(move.split(" ")[2]));
                if (!move.split(" ")[3].contains("nothing")) {
                    temp1.add(getStudentColorFromString(move.split(" ")[3]));
                }
                parameters.add(temp1);

                temp2.add(getStudentColorFromString(move.split(" ")[5]));
                if (!move.split(" ")[6].contains("nothing")) {
                    temp2.add(getStudentColorFromString(move.split(" ")[6]));
                }
                parameters.add(temp2);
            }
        }
        doCharacterMove(ID, parameters);
    }

    private StudentColor getStudentColorFromString (String color) {
        switch (color) {
            case "yellow":
                return StudentColor.YELLOW;
            case "blue":
                return StudentColor.BLUE;
            case "green":
                return StudentColor.GREEN;
            case "red":
                return StudentColor.RED;
            case "pink":
                return StudentColor.PINK;
            default:
                return null;
        }
    }

    private void handleMessageNotMove () {
        LOGGER.log(Level.FINE, "Handling a message received while waiting for input, interrupting thread that is waiting for input");
        terminal.writer().println(ansi().fgRgb(255, 0, 0).a("Received some message that is not a move...").fgDefault());
        terminal.writer().flush();
        waitEnterPressed();
    }

    private void askForMoveBasedOnState () {
        switch (model.getGameState().getGameStateName()) {

            case "PS" -> getMovePlayAssistant(terminal, commandsHistory, client.getUsername(), moveFromStdin);

            case "MSS" -> getMoveStudentToIsland(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);

            case "MMNS" -> getMoveMotherNature(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);

            case "CCS" -> getMoveSelectCloud(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);
        }
    }

    private void waitForMoveOrMessage () {
        Thread t = new Thread(new Runnable() {
            public void run () {
                try {
                    LOGGER.log(Level.SEVERE, "Thread: starting to read from stdin");
                    askForMoveBasedOnState();

                } catch (UserInterruptException e) {
                    LOGGER.log(Level.SEVERE, "Thread: Got interrupted signal in thread");
                    LOGGER.log(Level.SEVERE, e.toString());
                }
            }
        });

        client.getConnection().bindFunction(
                (Connection c) -> {
                    t.interrupt();
                    LOGGER.log(Level.SEVERE, "Sent interrupted signal to thread");
                    return false;
                }
        );

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Got InterruptedException while waiting for thread to join");
            throw new RuntimeException(e);
        }
    }

    private int findRoundNumber () {
        if (model.getGameState().getGameStateName().equals("PS")) {
            return 11 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
        }
        return 10 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
    }

    private int[] getCharactersCost () {
        Character[] characters = model.getCharacters();
        int[] cost = new int[characters.length];
        for (int i = 0; i < characters.length; i++) {
            cost[i] = characters[i].getPrize();
        }
        return cost;
    }

    private int[] getCharactersID () {
        if (model.getGameMode().equals(GameMode.COMPLETE)) {
            Character[] characters = model.getCharacters();
            int[] id = new int[characters.length];
            for (int i = 0; i < characters.length; i++) {
                id[i] = characters[i].getId();
            }
            return id;
        }
        return new int[0];
    }

    private HashMap<Integer, int[]> getColorsListFromCharacters (Character[] characters) {
        HashMap<Integer, int[]> hashmap = new HashMap<>();
        for (int i = 0; i < characters.length; i++) {
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
        }
        return hashmap;
    }

    private int[] getNoEntryTilesFromCharacters (Character[] characters) {
        int[] array = new int[characters.length];
        for (int i = 0; i < characters.length; i++) {
            Character c = characters[i];
            if(c.getId() == 5) {
                array[i] = ((CharacterFive) c).getStopNumber();
            }
            else {
                array[i] = -1;
            }
        }
        return array;
    }

    private void drawGameBoard (ArrayList<String> usernames, String username, String IP, int port) {
        int baseCol = (terminal.getWidth() - 192) / 2;
        int baseRow = 1;

        clearTerminal(terminal);

        terminal.writer().println(ansi().cursor(2, baseCol + 16).a("Dashboard").cursor(2, baseCol + 89).a("Eriantys Board").cursor(2, baseCol + 172).a("Players"));

        resetCursorColors(terminal);

        drawInfoSection(
                terminal,
                baseRow + 4,
                baseCol + 6,
                IP,
                port,
                model.getGameMode().toString(),
                model.getPlayerNumber().getWizardNumber(),
                findRoundNumber(),
                client.getUsernames().get(model.getGameState().getCurrentPlayer()),
                model.getGameState().getGameStateName(),
                model.getGameMode().equals(GameMode.COMPLETE) ? getCharactersID() : null,
                model.getGameMode().equals(GameMode.COMPLETE) ? getCharactersCost() : null,
                model.getWizard(client.getUsernames().indexOf(client.getUsername())).getCardDeck().getDeckCards(),
                model.getGameMode().equals(GameMode.COMPLETE) ? getColorsListFromCharacters(model.getCharacters()) : null,
                model.getGameMode().equals(GameMode.COMPLETE) ? getNoEntryTilesFromCharacters(model.getCharacters()) : null

        );

        drawTilesAndClouds(baseRow + 4, baseCol + 44, model);

        drawPlayerBoards(baseRow + 4, baseCol + 157, model, usernames);
    }

    private void drawPlayerBoards (int baseRow, int baseCol, Game model, ArrayList<String> usernames) {
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
                    model.getGameState().getCurrentPlayer() == client.getUsernames().indexOf(client.getUsername()) ? (model.getGameState() instanceof ActionState ? (((ActionState) model.getGameState()).getActivatedCharacter() != null ? ((ActionState) model.getGameState()).getActivatedCharacter().getId() : -1) : -1) : -1,
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
                    });
        }
    }

    private void drawTilesAndClouds (int baseRow, int baseCol, Game model) {
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

        drawTilesBridges(baseRow, baseCol, model);

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

    private boolean areIslandsConnected (Game model, int firstIsl, int secondIsl) {
        for (IslandGroup g : model.getIslandGroupList()) {
            if (2 == g.getIslandList().stream().map(island -> island.getId()).filter(integer -> integer == firstIsl || integer == secondIsl).count()) {
                return true;
            }
        }
        return false;
    }

    private void drawTilesBridges (int baseRow, int baseCol, Game model) {
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


}

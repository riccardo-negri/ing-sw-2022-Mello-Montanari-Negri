package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.model.entity.*;
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
                printConsoleInfo(terminal, "Waiting for other player to do a move...");

                Message message = client.getConnection().waitMessage();

                if (message instanceof Disconnected) {
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

        onEnd();

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

            case "MSS" -> getMoveStudentToIsland(terminal, commandsHistory, client.getUsername(),getCharactersID(), moveFromStdin);

            case "MMNS" -> getMoveMotherNature(terminal, commandsHistory, client.getUsername(),getCharactersID(),  moveFromStdin);

            case "CCS" -> getMoveSelectCloud(terminal, commandsHistory, client.getUsername(),getCharactersID(),  moveFromStdin);
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

    private int findRoundNumber() {
        if(model.getGameState().getGameStateName().equals("PS")) {
            return 11 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
        }
        return 10 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
    }

    private int[] getCharactersCost() {
        Character[] characters = model.getCharacters();
        int[] cost = new int[characters.length];
        for (int i=0; i < characters.length; i++) {
            cost[i] = characters[i].getPrize();
        }
        return cost;
    }

    private int[] getCharactersID() {
        if(model.getGameMode().equals(GameMode.COMPLETE)) {
            Character[] characters = model.getCharacters();
            int[] id = new int[characters.length];
            for (int i=0; i < characters.length; i++) {
                id[i] = characters[i].getId();
            }
            return id;
        }
        return new int[0];
    }

    private void drawGameBoard (ArrayList<String> usernames, String username, String IP, int port) {
        int baseCol = terminal.getWidth() / 2 - 200 / 2;
        int baseRow = 1;

        clearTerminal(terminal);

        terminal.writer().println(ansi().cursor(2, 34).a("Dashboard").cursor(2, 110).a("Eriantys Board").cursor(2, 188).a("Players"));

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
                getCharactersCost(),
                model.getWizard(client.getUsernames().indexOf(client.getUsername())).getCardDeck().getDeckCards()
        );

        drawTilesAndClouds(baseCol + 61, baseRow + 3, model);

        drawPlayerBoards(baseCol + 155, baseRow + 4, model, usernames);
    }

    private void drawPlayerBoards (int baseCol, int baseRow, Game model, ArrayList<String> usernames) {
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
                    model.getGameState() instanceof ActionState ? (((ActionState) model.getGameState()).getActivatedCharacter() != null ? ((ActionState) model.getGameState()).getActivatedCharacter().getId() : -1) : -1,
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

    private void drawTilesAndClouds (int baseCol, int baseRow, Game model) {
        HashMap<Integer, List<Integer>> relativePlacementOfIslandBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        relativePlacementOfIslandBasedOnID.put(0, List.of(0, 0));
        relativePlacementOfIslandBasedOnID.put(1, List.of(0, (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(2, List.of(0, 2 * (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(3, List.of(0, 3 * (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(4, List.of((getIslandHeight() + 1), 3 * (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(5, List.of(2 * (getIslandHeight() + 1), 3 * (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(6, List.of(3 * (getIslandHeight() + 1), 3 * (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(7, List.of(3 * (getIslandHeight() + 1), 2 * (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(8, List.of(3 * (getIslandHeight() + 1), (getIslandWidth() + 2)));
        relativePlacementOfIslandBasedOnID.put(9, List.of(3 * (getIslandHeight() + 1), 0));
        relativePlacementOfIslandBasedOnID.put(10, List.of(2 * (getIslandHeight() + 1), 0));
        relativePlacementOfIslandBasedOnID.put(11, List.of((getIslandHeight() + 1), 0));

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
                        model.getFistIslandGroup().equals(g)
                );

            }
        }

        HashMap<Integer, List<Integer>> relativePlacementOfCloudBasedOnID = new HashMap<>(); // first int of list is row offset, second int is column offset
        relativePlacementOfCloudBasedOnID.put(0, List.of(2 + (getIslandHeight() + 1), 3 + (getIslandWidth() + 2)));
        relativePlacementOfCloudBasedOnID.put(1, List.of(2 + 2 * (getIslandHeight() + 1), 3 + (getIslandWidth() + 2)));
        relativePlacementOfCloudBasedOnID.put(2, List.of(2 + (getIslandHeight() + 1), 3 + 2 * (getIslandWidth() + 2)));
        relativePlacementOfCloudBasedOnID.put(3, List.of(2 + 2 * (getIslandHeight() + 1), 3 + 2 * (getIslandWidth() + 2)));

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

}

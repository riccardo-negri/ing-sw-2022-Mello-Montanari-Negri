package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import it.polimi.ingsw.model.entity.*;
import it.polimi.ingsw.model.entity.gameState.PlanningState;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.StudentColor;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

import javax.imageio.ImageTranscoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.polimi.ingsw.client.ui.cli.utils.BoardUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.MoveUtilsCLI.*;
import static java.lang.Thread.sleep;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageCLI extends AbstractBoardPage {

    public BoardPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        CLI cli = (CLI) client.getUI();
        Terminal terminal = cli.getTerminal();
        Game model = client.getModel();

        while(!client.getModel().isGameEnded()) {
            drawGameBoard(terminal, model, client.getUsernames(), client.getUsername(), client.getIPAddress(), client.getPort());

            drawConsoleArea(terminal, 48);
            System.out.println("HERE");
            final Object moveReadLock = new Object();
            String moveRead = null;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        getMovePlayAssistant(terminal, moveRead, moveReadLock);
                    }
                    catch (UserInterruptException e) {
                        System.err.println(e.getMessage());
                    }
                }
            });
            t.start();
            client.getLogger().log(Level.INFO, "Test");
            synchronized(moveReadLock){
                while (moveRead == null && !client.getConnection().hasMessagesToProcess()){
                    try {
                        moveReadLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("HERE1");

            //////////////////////////
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.interrupt();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(t.isAlive());

            System.out.println("HERE");
            waitEnterPressed();
            System.out.println("AFTERENTERPRESSED");

            while ( moveRead == null) {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (model.getGameState().getClass().equals(PlanningState.class)) {
                Matcher matcher;
                String move;
                do {
                    getMovePlayAssistant(terminal, moveRead, moveReadLock);
                    Pattern pattern = Pattern.compile("play assistant [0-9]", Pattern.CASE_INSENSITIVE);
                    matcher = pattern.matcher(moveRead);
                }
                while (!matcher.find());
                try {
                    doCardChoice(Integer.parseInt(moveRead.split(" ")[2]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                getMoveStudentToIsland(terminal, moveRead, moveReadLock);
                getMoveMotherNature(terminal, moveRead, moveReadLock);
                getMoveSelectCloud(terminal, moveRead, moveReadLock);
            }
            waitEnterPressed();
        }

        onEnd();

    }

    private void drawGameBoard (Terminal terminal, Game model, ArrayList<String> usernames, String username, String IP, int port) {
        int baseCol = terminal.getWidth() / 2 - 200 / 2;
        int baseRow = 1;

        clearTerminal(terminal);

        terminal.writer().println(ansi().cursor(2, 34).a("Dashboard").cursor(2, 110).a("Eriantys Board").cursor(2, 188).a("Players"));

        resetCursorColors(terminal);

        drawInfoSection(
                terminal,
                baseRow + 4,
                baseCol + 6,
                "7", // TODO how do i find this
                IP,
                port,
                model.getGameMode().toString(),
                model.getPlayerNumber().getWizardNumber(),
                3, //TODO add round to model
                "Tom", //TODO how do I know who is playing
                "move students", //TODO add getters in GameState
                model.getGameMode().equals(GameMode.COMPLETE) ? new String[]{"Clown", "Wizard", "Knight"} : new String[]{}, //TODO its missing a method to get the characters even if i don't know the ID
                new int[]{3, 1, 2}, // TODO here
                new int[]{1, 2, 3, 4, 7, 8, 10} //TODO create getter to find card in a deck model.getWizard(usernames.indexOf(username)).getCardDeck().getCards() for example
        );

        drawTilesAndClouds(terminal, baseCol + 61, baseRow + 3, model);

        drawPlayerBoards(terminal, baseCol + 155, baseRow + 4, model, usernames);
    }

    private void drawPlayerBoards (Terminal terminal, int baseCol, int baseRow, Game model, ArrayList<String> usernames) {
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
                    "9 (clown)", //TODO here
                    w.getTowerColor().toString(),
                    w.getTowerNumber(),
                    new boolean[]{
                            model.getProfessor(StudentColor.YELLOW).getMaster(null) != null && model.getProfessor(StudentColor.YELLOW).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.BLUE).getMaster(null) != null && model.getProfessor(StudentColor.BLUE).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.GREEN).getMaster(null) != null && model.getProfessor(StudentColor.GREEN).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.RED).getMaster(null) != null && model.getProfessor(StudentColor.RED).getMaster(null).equals(w),
                            model.getProfessor(StudentColor.PINK).getMaster(null) != null && model.getProfessor(StudentColor.PINK).getMaster(null).equals(w)
                    },
                    new int[]{
                            w.getDiningStudents(StudentColor.YELLOW),
                            w.getDiningStudents(StudentColor.BLUE),
                            w.getDiningStudents(StudentColor.GREEN),
                            w.getDiningStudents(StudentColor.RED),
                            w.getDiningStudents(StudentColor.PINK)
                    },
                    new int[]{
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(2)).count(), // green
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(3)).count(), // red
                            (int) w.getEntranceStudents().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                    });
        }

        /*drawSinglePlayerArea(terminal, baseRow, baseCol, "Ric", "10 (5 steps)", 1, "9 (clown)", "WHITE", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(terminal, baseRow + getSchoolBoardHeight() + 2, baseCol, "Tom", "4 (2 steps)", 1, "9 (clown)", "BLACK", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(terminal, baseRow + 2 * (getSchoolBoardHeight() + 2), baseCol, "Pietro", "3 (2 steps)", 1, "9 (clown)", "BLACK", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(terminal, baseRow + 3 * (getSchoolBoardHeight() + 2), baseCol, "Sanp", "6 (3 steps)", 1, "", "WHITE", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        */
    }

    private void drawTilesAndClouds (Terminal terminal, int baseCol, int baseRow, Game model) {
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
        /*
        drawIsland(terminal, baseRow, baseCol, 1, "none", 3, 1, 2, 4, 1, true);
        drawIsland(terminal, baseRow, baseCol + (getIslandWidth() + 2), 2, "black", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow, baseCol + 2 * (getIslandWidth() + 2), 3, "none", 1, 0, 1, 0, 0, false);
        drawIsland(terminal, baseRow, baseCol + 3 * (getIslandWidth() + 2), 4, "none", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow + (getIslandHeight() + 1), baseCol + 3 * (getIslandWidth() + 2), 5, "grey", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow + 2 * (getIslandHeight() + 1), baseCol + 3 * (getIslandWidth() + 2), 6, "white", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow + 3 * (getIslandHeight() + 1), baseCol + 3 * (getIslandWidth() + 2), 7, "none", 1, 1, 2, 1, 1, false);
        drawIsland(terminal, baseRow + 3 * (getIslandHeight() + 1), baseCol + 2 * (getIslandWidth() + 2), 8, "white", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow + 3 * (getIslandHeight() + 1), baseCol + (getIslandWidth() + 2), 9, "none", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow + 3 * (getIslandHeight() + 1), baseCol, 10, "black", 0, 0, 0, 1, 1, false);
        drawIsland(terminal, baseRow + 2 * (getIslandHeight() + 1), baseCol, 11, "none", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, baseRow + (getIslandHeight() + 1), baseCol, 12, "black", 0, 1, 2, 0, 1, false);*/

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
        /*drawCloud(terminal, 1, baseRow + 2 + (getIslandHeight() + 1), baseCol + 3 + (getIslandWidth() + 2), 1, 1, 1, 1, 1);
        drawCloud(terminal, 2, baseRow + 2 + 2 * (getIslandHeight() + 1), baseCol + 3 + (getIslandWidth() + 2), 1, 1, 0, 1, 1);
        drawCloud(terminal, 3, baseRow + 2 + (getIslandHeight() + 1), baseCol + 3 + 2 * (getIslandWidth() + 2), 4, 0, 0, 0, 1);
        drawCloud(terminal, 4, baseRow + 2 + 2 * (getIslandHeight() + 1), baseCol + 3 + 2 * (getIslandWidth() + 2), 1, 0, 2, 2, 0);*/
    }

}

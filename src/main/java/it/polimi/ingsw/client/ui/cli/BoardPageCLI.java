package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.ui.cli.utils.BoardUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.MoveUtilsCLI.*;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageCLI extends AbstractWelcomePage {

    public BoardPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        CLI cli = (CLI) client.getUI();
        Terminal terminal = cli.getTerminal();

        drawGameBoard(terminal);

        drawConsoleArea(terminal, 48);

        getMovePlayAssistant(terminal);
        getMoveStudentToIsland(terminal);
        getMoveMotherNature(terminal);
        getMoveSelectCloud(terminal);

        waitEnterPressed();
    }

    private void drawGameBoard(Terminal terminal) {
        int baseCol = terminal.getWidth()/2-200/2;
        int baseRow = 1;
        clearTerminal(terminal);
        terminal.writer().println(ansi().cursor(2, 34).a("Dashboard").cursor(2, 110).a("Eriantys Board").cursor(2, 188).a("Players"));
        resetCursorColors(terminal);
        drawInfoSection(terminal, baseRow+4, baseCol+6, "7", "localhost", 50547, "advanced", 4, 3, "Tom", "move students", new String[]{"Clown", "Wizard", "Knight"}, new int[]{3, 1, 2});
        drawTilesAndClouds(terminal, baseCol+61, baseRow+3);
        drawPlayers(terminal, baseCol+155, baseRow+4);
    }

    private void drawPlayers (Terminal terminal, int baseCol, int baseRow) {
        drawSinglePlayerArea(terminal, baseRow, baseCol, "Ric", "10 (5 steps)", 1, "9 (clown)", "white", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(terminal, baseRow + getSchoolBoardHeight() + 2, baseCol, "Tom", "4 (2 steps)", 1, "9 (clown)", "black", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(terminal, baseRow + 2 * (getSchoolBoardHeight() + 2), baseCol, "Pietro", "3 (2 steps)", 1, "9 (clown)", "black", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(terminal, baseRow + 3 * (getSchoolBoardHeight() + 2), baseCol, "Sanp", "6 (3 steps)", 1, "", "white", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
    }

    private void drawTilesAndClouds (Terminal terminal, int baseCol, int baseRow) {
        drawIsland(terminal, 1, baseRow, baseCol, "none", 3, 1, 2, 4, 1, true);
        drawIsland(terminal, 2, baseRow, baseCol + (getIslandWidth() + 2), "black", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 3, baseRow, baseCol + 2 * (getIslandWidth() + 2), "none", 1, 0, 1, 0, 0, false);
        drawIsland(terminal, 4, baseRow, baseCol + 3 * (getIslandWidth() + 2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 5, baseRow + (getIslandHeight() + 1), baseCol + 3 * (getIslandWidth() + 2), "grey", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 6, baseRow + 2 * (getIslandHeight() + 1), baseCol + 3 * (getIslandWidth() + 2), "white", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 7, baseRow + 3 * (getIslandHeight() + 1), baseCol + 3 * (getIslandWidth() + 2), "none", 1, 1, 2, 1, 1, false);
        drawIsland(terminal, 8, baseRow + 3 * (getIslandHeight() + 1), baseCol + 2 * (getIslandWidth() + 2), "white", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 9, baseRow + 3 * (getIslandHeight() + 1), baseCol + (getIslandWidth() + 2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 10, baseRow + 3 * (getIslandHeight() + 1), baseCol, "black", 0, 0, 0, 1, 1, false);
        drawIsland(terminal, 11, baseRow + 2 * (getIslandHeight() + 1), baseCol, "none", 0, 1, 2, 0, 1, false);
        drawIsland(terminal, 12, baseRow + (getIslandHeight() + 1), baseCol, "black", 0, 1, 2, 0, 1, false);

        drawCloud(terminal, 1, baseRow + 2 + (getIslandHeight() + 1), baseCol + 3 + (getIslandWidth() + 2), 4, 1, 1, 1, 1, 1);
        drawCloud(terminal, 2, baseRow + 2 + 2 * (getIslandHeight() + 1), baseCol + 3 + (getIslandWidth() + 2), 4, 1, 1, 0, 1, 1);
        drawCloud(terminal, 3, baseRow + 2 + (getIslandHeight() + 1), baseCol + 3 + 2 * (getIslandWidth() + 2), 4, 4, 0, 0, 0, 1);
        drawCloud(terminal, 4, baseRow + 2 + 2 * (getIslandHeight() + 1), baseCol + 3 + 2 * (getIslandWidth() + 2), 4, 1, 0, 2, 2, 0);
    }

}

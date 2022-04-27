package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import org.fusesource.jansi.AnsiConsole;

import static it.polimi.ingsw.client.ui.cli.BoardUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.BoardUtilsCLI.getSchoolBoardHeight;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageCLI extends AbstractWelcomePage {

    public BoardPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        CoreCLI cli;
        cli = (CoreCLI) client.getUI();

        cli.clearTerminal();
        AnsiConsole.out().println(ansi().cursor(2,12).a("Dashboard").cursor(2,76).a("Eriantys Board").cursor(2, 151).a("Players"));
        cli.resetCursorColors();
        drawInfoSection(4, 2, "7", "localhost", 50547, "advanced", 4, 3, "Tom", "move students", new String[]{"Clown", "Wizard", "Knight"}, new int[]{3,1,2});
        drawTilesAndClouds(45, 3);
        drawPlayers(135, 4);
        cli.moveCursorToEnd();
        cli.waitEnterPressed();
    }

    private void drawPlayers(int startX, int startY) {
        drawSinglePlayerArea(startY, startX, "Ric", "10 (5 steps)", 1, "9 (clown)", "white", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(startY+getSchoolBoardHeight()+2, startX, "Tom", "4 (2 steps)", 1, "9 (clown)", "black", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(startY+2*(getSchoolBoardHeight()+2), startX, "Pietro", "3 (2 steps)", 1, "9 (clown)", "black", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
        drawSinglePlayerArea(startY+3*(getSchoolBoardHeight()+2), startX, "Sanp", "6 (3 steps)", 1, "", "white", 4, new boolean[]{true, false, true, true, false}, new int[]{3, 0, 2, 1, 0}, new int[]{1, 2, 1, 0, 3});
    }

    private void drawTilesAndClouds (int BoardBaseX, int BoardBaseY) {
        drawIsland(1, BoardBaseY,BoardBaseX, "none", 3, 1, 2, 4, 1, true);
        drawIsland(2, BoardBaseY,BoardBaseX+(getIslandWidth()+2), "black", 0, 1, 2, 0, 1, false);
        drawIsland(3, BoardBaseY,BoardBaseX+2*(getIslandWidth()+2), "none", 1, 0, 1, 0, 0, false);
        drawIsland(4, BoardBaseY,BoardBaseX+3*(getIslandWidth()+2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(5, BoardBaseY+(getIslandHeight()+1),BoardBaseX+3*(getIslandWidth()+2), "grey", 0, 1, 2, 0, 1, false);
        drawIsland(6, BoardBaseY+2*(getIslandHeight()+1),BoardBaseX+3*(getIslandWidth()+2), "white", 0, 1, 2, 0, 1, false);
        drawIsland(7, BoardBaseY+3*(getIslandHeight()+1),BoardBaseX+3*(getIslandWidth()+2), "none", 1, 1, 2, 1, 1, false);
        drawIsland(8, BoardBaseY+3*(getIslandHeight()+1),BoardBaseX+2*(getIslandWidth()+2), "white", 0, 1, 2, 0, 1, false);
        drawIsland(9, BoardBaseY+3*(getIslandHeight()+1),BoardBaseX+(getIslandWidth()+2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(10, BoardBaseY+3*(getIslandHeight()+1),BoardBaseX, "black", 0, 0, 0, 1, 1, false);
        drawIsland(11, BoardBaseY+2*(getIslandHeight()+1),BoardBaseX, "none", 0, 1, 2, 0, 1, false);
        drawIsland(12, BoardBaseY+(getIslandHeight()+1),BoardBaseX, "black", 0, 1, 2, 0, 1, false);

        drawCloud(1, BoardBaseY+2+(getIslandHeight()+1),BoardBaseX+3+(getIslandWidth()+2), 4, 1, 1, 1, 1, 1);
        drawCloud(2, BoardBaseY+2+2*(getIslandHeight()+1),BoardBaseX+3+(getIslandWidth()+2), 4, 1, 1, 0, 1, 1);
        drawCloud(3, BoardBaseY+2+(getIslandHeight()+1),BoardBaseX+3+2*(getIslandWidth()+2), 4, 4, 0, 0, 0, 1);
        drawCloud(4, BoardBaseY+2+2*(getIslandHeight()+1),BoardBaseX+3+2*(getIslandWidth()+2), 4, 1, 0, 2, 2, 0);
    }

}

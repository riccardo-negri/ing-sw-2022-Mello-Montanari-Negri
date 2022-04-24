package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;

import static it.polimi.ingsw.client.ui.cli.BoardUtilsCLI.*;
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
        cli.moveCursorToTop();
        cli.printTerminalCenteredLine("Eriantys Board");
        cli.resetCursorColors();
        drawTilesAndClouds(cli.getTerminalWidth()/2-77/2, 2);
        cli.moveCursorToEnd();
        cli.waitKeyPressed();
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

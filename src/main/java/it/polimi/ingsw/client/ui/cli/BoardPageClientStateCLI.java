package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.states.AbstractWelcomePageClientState;
import org.fusesource.jansi.AnsiConsole;

import static it.polimi.ingsw.client.ui.cli.BoardUtilsCLI.*;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageClientStateCLI extends AbstractWelcomePageClientState {

    public BoardPageClientStateCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        CoreCLI cli;
        cli = (CoreCLI) client.getUI();
        cli.clearTerminal();
        drawIsland(1, 2,2, "none", 0, 1, 2, 0, 1, true);
        drawIsland(2, 2,2+(getIslandWidth()+2), "black", 0, 1, 2, 0, 1, false);
        drawIsland(3, 2,2+2*(getIslandWidth()+2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(4, 2,2+3*(getIslandWidth()+2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(5, 2+(getIslandHeight()+1),2+3*(getIslandWidth()+2), "grey", 0, 1, 2, 0, 1, false);
        drawIsland(6, 2+2*(getIslandHeight()+1),2+3*(getIslandWidth()+2), "white", 0, 1, 2, 0, 1, false);
        drawIsland(7, 2+3*(getIslandHeight()+1),2+3*(getIslandWidth()+2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(8, 2+3*(getIslandHeight()+1),2+2*(getIslandWidth()+2), "white", 0, 1, 2, 0, 1, false);
        drawIsland(9, 2+3*(getIslandHeight()+1),2+(getIslandWidth()+2), "none", 0, 1, 2, 0, 1, false);
        drawIsland(10, 2+3*(getIslandHeight()+1),2, "black", 0, 1, 2, 0, 1, false);
        drawIsland(11, 2+2*(getIslandHeight()+1),2, "none", 0, 1, 2, 0, 1, false);
        drawIsland(12, 2+(getIslandHeight()+1),2, "black", 0, 1, 2, 0, 1, false);
        AnsiConsole.out().println(ansi().cursor(1000,1000));
        cli.waitKeyPressed();
    }

}

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractGameModeSelectionPage;

public class GameModeSelectionPageCLI extends AbstractGameModeSelectionPage {

    public GameModeSelectionPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        String GAME_MODE =
                " ██████╗  █████╗ ███╗   ███╗███████╗    ███╗   ███╗ ██████╗ ██████╗ ███████╗\n" +
                "██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ████╗ ████║██╔═══██╗██╔══██╗██╔════╝\n" +
                "██║  ███╗███████║██╔████╔██║█████╗      ██╔████╔██║██║   ██║██║  ██║█████╗  \n" +
                "██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║╚██╔╝██║██║   ██║██║  ██║██╔══╝  \n" +
                "╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ██║ ╚═╝ ██║╚██████╔╝██████╔╝███████╗\n" +
                " ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝    ╚═╝     ╚═╝ ╚═════╝ ╚═════╝ ╚══════╝\n";
        CoreCLI cli = (CoreCLI) client.getUI();

        cli.clearTerminal();
        cli.printTerminalCenteredMultilineText(GAME_MODE, 5);
        cli.printEmptyLine();
        int  playersNumber = cli.readNumber("Please select the number of players (min 2 players, max 4 players):", 2, 4, null);
        boolean isAdvancedGame = cli.readBoolean("Do you want to play the advanced game rules (y or n)?");
        cli.printEmptyLine();
        cli.printTerminalCenteredLine("Press any key to join the matchmaking queue...");
        cli.waitKeyPressed();
        onEnd();
    }
}

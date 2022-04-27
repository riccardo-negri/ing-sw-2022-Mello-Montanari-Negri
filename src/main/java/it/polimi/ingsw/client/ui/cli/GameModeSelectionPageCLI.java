package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractGameModeSelectionPage;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

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

        clearTerminal();
        printTerminalCenteredMultilineText(GAME_MODE, 5);
        printEmptyLine();
        int  playersNumber = readNumber("Please select the number of players (min 2 players, max 4 players):", 2, 4, null);
        boolean isAdvancedGame = readBoolean("Do you want to play the advanced game rules (y or n)?");
        printEmptyLine();
        printTerminalCenteredLine("Press enter to join the matchmaking queue...");
        waitEnterPressed();
        onEnd(playersNumber, isAdvancedGame);
    }
}

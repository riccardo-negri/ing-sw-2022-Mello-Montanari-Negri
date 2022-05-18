package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractGameModeSelectionPage;
import org.jline.terminal.Terminal;

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

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, GAME_MODE, 5);
        printEmptyLine(terminal);
        int playersNumber = readNumber(terminal, "Please select the number of players (min 2 players, max 4 players) (default is 2):", 2, 4, 2);
        boolean isAdvancedGame = readBoolean(terminal, "Do you want to play the advanced game rules (y or n)? (default is y)", true); // TODO print default
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "Press enter to join the matchmaking queue...");
        waitEnterPressed(terminal);
        onEnd(playersNumber, isAdvancedGame);
    }
}

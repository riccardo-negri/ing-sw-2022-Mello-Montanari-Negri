package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreateGamePage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class CreateGamePageCLI extends AbstractCreateGamePage {

    public CreateGamePageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        final String CREATE_GAME = """
                 ██████╗██████╗ ███████╗ █████╗ ████████╗███████╗     ██████╗  █████╗ ███╗   ███╗███████╗
                ██╔════╝██╔══██╗██╔════╝██╔══██╗╚══██╔══╝██╔════╝    ██╔════╝ ██╔══██╗████╗ ████║██╔════╝
                ██║     ██████╔╝█████╗  ███████║   ██║   █████╗      ██║  ███╗███████║██╔████╔██║█████╗\040\040
                ██║     ██╔══██╗██╔══╝  ██╔══██║   ██║   ██╔══╝      ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝\040\040
                ╚██████╗██║  ██║███████╗██║  ██║   ██║   ███████╗    ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗
                 ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝     ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝
                """;

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, CREATE_GAME, 5);
        printEmptyLine(terminal);
        int playersNumber = readNumber(terminal, "Please select the number of players (min 2 players, max 4 players) (default is 2):", 2, 4, 2);
        boolean isAdvancedGame = readBoolean(terminal, "Do you want to play the advanced game rules (y or n)? (default is y)", true);
        createGameAndGoToLobby(playersNumber, isAdvancedGame);
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "Successfully created the game\n");
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "Press enter to join the lobby...");
        waitEnterPressed(terminal);
        onEnd();
    }
}

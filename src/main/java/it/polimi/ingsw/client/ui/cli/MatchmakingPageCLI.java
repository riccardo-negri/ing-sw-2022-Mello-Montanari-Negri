package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMatchmakingPage;
import it.polimi.ingsw.client.page.ClientPage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class MatchmakingPageCLI extends AbstractMatchmakingPage {
    public MatchmakingPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        final String GAME_MODE = """
                ███╗   ███╗ █████╗ ████████╗ ██████╗██╗  ██╗███╗   ███╗ █████╗ ██╗  ██╗██╗███╗   ██╗ ██████╗\040
                ████╗ ████║██╔══██╗╚══██╔══╝██╔════╝██║  ██║████╗ ████║██╔══██╗██║ ██╔╝██║████╗  ██║██╔════╝\040
                ██╔████╔██║███████║   ██║   ██║     ███████║██╔████╔██║███████║█████╔╝ ██║██╔██╗ ██║██║  ███╗
                ██║╚██╔╝██║██╔══██║   ██║   ██║     ██╔══██║██║╚██╔╝██║██╔══██║██╔═██╗ ██║██║╚██╗██║██║   ██║
                ██║ ╚═╝ ██║██║  ██║   ██║   ╚██████╗██║  ██║██║ ╚═╝ ██║██║  ██║██║  ██╗██║██║ ╚████║╚██████╔╝
                ╚═╝     ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝\040
                """;

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, GAME_MODE, 1);
        printEmptyLine(terminal);
        while (client.getNextState() == ClientPage.MATCHMAKING_PAGE) {
            try {
                connectToMatchmakingServer();
                printTerminalCenteredLine(terminal, "Successfully connected to the matchmaking server\n");
                break;
            } catch (java.lang.RuntimeException e) {
                boolean retry = readBoolean(terminal, "Failed to connect to the matchmaking server, do you want to retry (y or n)?", null);
                if (!retry) {
                    onEnd(false);
                }
            }
        }
        while (client.getNextState() == ClientPage.MATCHMAKING_PAGE) {
            try {
                connectToGameServer();
                printTerminalCenteredLine(terminal, "Successfully connected to the game server\n");
                printTerminalCenteredLine(terminal, "Waiting for the game to start...\n");
                waitForStartAndLoadInitialState();
                onEnd(true);
                break;
            } catch (java.lang.RuntimeException e) {
                boolean retry = readBoolean(terminal, "Failed to connect to the game server, do you want to retry (y or n)?", null);
                if (!retry) {
                    onEnd(false);
                }
            }
        }

    }
}

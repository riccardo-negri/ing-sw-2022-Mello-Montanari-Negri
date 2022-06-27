package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;

import static it.polimi.ingsw.client.page.ClientPage.*;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class ConnectionPageCLI extends AbstractConnectionPage {

    public ConnectionPageCLI (Client client) {
        super(client);
    }

    /**
     * draw the page and handle all expected actions
     */
    @Override
    public void draw () {
        final String CONNECTION = """
                 ██████╗ ██████╗ ███╗   ██╗███╗   ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗
                ██╔════╝██╔═══██╗████╗  ██║████╗  ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║
                ██║     ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║
                ██║     ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║
                ╚██████╗╚██████╔╝██║ ╚████║██║ ╚████║███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║
                 ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝
                """;

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, CONNECTION, 5);
        printEmptyLine(terminal);
        String ipAddress = readIPAddress(terminal);
        int portNumber = readNumber(terminal, "Please insert the port number of the server (default is 50000):", 49152, 65535, 50000);

        boolean firstRun = true;
        do {
            if (!firstRun) printTerminalCenteredLine(terminal, "Username already taken\n");
            firstRun = false;
            String username = readGenericString(terminal, "What is your username for the game?");

            printEmptyLine(terminal);
            try {
                connectToMatchmakingServer(ipAddress, portNumber, username);
                printTerminalCenteredLine(terminal, "Successfully connected to the matchmaking server\n");
            } catch (java.lang.RuntimeException e) {
                printTerminalCenteredLine(terminal, "Failed to connect to the matchmaking server\n");
                printTerminalCenteredLine(terminal, "Press enter to retry...");
                waitEnterPressed(terminal);
                client.setNextState(CONNECTION_PAGE);
                return;
            }

            printEmptyLine(terminal);
        } while (!waitForLobbiesListOrRedirect());

        if (client.getNextState() == LOBBY_PAGE) {
            printTerminalCenteredLine(terminal, "You are already registered on the server! Welcome back\n");
            printTerminalCenteredLine(terminal, "Press enter to continue...");
            waitEnterPressed(terminal);
        }
        else {
            boolean createGame = readBoolean(terminal, "Do you want to create a new game (y to create, n to join a lobby)? (default is y)", true);
            onEnd(createGame);
        }

    }
}

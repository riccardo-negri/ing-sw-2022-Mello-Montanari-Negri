package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class ConnectionPageCLI extends AbstractConnectionPage {

    public ConnectionPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
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
        String username = readGenericString(terminal, "What is your username for the game?");
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "Press enter to continue...");
        waitEnterPressed(terminal);
        onEnd(ipAddress, portNumber, username);
    }
}

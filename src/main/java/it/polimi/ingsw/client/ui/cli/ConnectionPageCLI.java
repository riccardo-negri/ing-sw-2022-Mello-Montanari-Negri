package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class ConnectionPageCLI extends AbstractConnectionPage{

    public ConnectionPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        String CONNECTION =
                " ██████╗ ██████╗ ███╗   ██╗███╗   ██╗███████╗ ██████╗████████╗██╗ ██████╗ ███╗   ██╗\n" +
                "██╔════╝██╔═══██╗████╗  ██║████╗  ██║██╔════╝██╔════╝╚══██╔══╝██║██╔═══██╗████╗  ██║\n" +
                "██║     ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║        ██║   ██║██║   ██║██╔██╗ ██║\n" +
                "██║     ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║        ██║   ██║██║   ██║██║╚██╗██║\n" +
                "╚██████╗╚██████╔╝██║ ╚████║██║ ╚████║███████╗╚██████╗   ██║   ██║╚██████╔╝██║ ╚████║\n" +
                " ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝ ╚═════╝   ╚═╝   ╚═╝ ╚═════╝ ╚═╝  ╚═══╝\n";

        clearTerminal();
        printTerminalCenteredMultilineText(CONNECTION, 5);
        printEmptyLine();
        String IPAddress = readIPAddress();
        int PortNumber = readNumber("Please insert the port number of the server (default is 50000):", 49152, 65535, 50000);
        String username = readGenericString("What is your username for the game?");
        printEmptyLine();
        printTerminalCenteredLine("Press enter to continue...");
        waitEnterPressed();
        onEnd(IPAddress, PortNumber, username);
    }
}

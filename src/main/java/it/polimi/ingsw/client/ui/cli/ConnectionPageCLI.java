package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractConnectionPage;

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
        CoreCLI cli = (CoreCLI) client.getUI();

        cli.clearTerminal();
        cli.printTerminalCenteredMultilineText(CONNECTION, 5);
        cli.printEmptyLine();
        String IPAddress = cli.readIPAddress();
        int PortNumber = cli.readNumber("Please insert the port number of the server (default is 50000):", 49152, 65535, 50000);
        String username = cli.readGenericString("What is your username for the game?");
        cli.printEmptyLine();
        cli.printTerminalCenteredLine("Press enter to continue...");
        //cli.bringCursorToMid(12);
        cli.waitEnterPressed();
        onEnd(IPAddress, PortNumber, username);
    }
}

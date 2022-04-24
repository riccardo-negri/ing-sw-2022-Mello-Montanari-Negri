package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.pages.AbstractConnectionPage;

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
        cli.printTerminalCenteredMultilineText(CONNECTION);
        cli.printEmptyLine();
        String IPAddress = cli.readIPAddress();
        int PortNumber = cli.readPortNumber();
        cli.printEmptyLine();
        cli.printTerminalCenteredLine("Attempting to connect to the server...");
        //cli.bringCursorToMid(12);
        cli.waitKeyPressed();
        onEnd();
    }
}

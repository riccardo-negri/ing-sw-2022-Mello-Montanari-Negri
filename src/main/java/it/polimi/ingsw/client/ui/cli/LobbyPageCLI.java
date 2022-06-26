package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractLobbyPage;
import it.polimi.ingsw.model.enums.GameMode;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class LobbyPageCLI extends AbstractLobbyPage {
    public LobbyPageCLI (Client client) {
        super(client);
    }

    /**
     * draw the page and handle all expected actions
     */
    @Override
    public void draw () {
        final String LOBBY = """
                 ██████╗  █████╗ ███╗   ███╗███████╗    ██╗      ██████╗ ██████╗ ██████╗ ██╗   ██╗
                ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██║     ██╔═══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
                ██║  ███╗███████║██╔████╔██║█████╗      ██║     ██║   ██║██████╔╝██████╔╝ ╚████╔╝\040
                ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║     ██║   ██║██╔══██╗██╔══██╗  ╚██╔╝\040\040
                ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ███████╗╚██████╔╝██████╔╝██████╔╝   ██║\040\040\040
                 ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝    ╚══════╝ ╚═════╝ ╚═════╝ ╚═════╝    ╚═╝\040\040\040
                """;

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, LOBBY, 1);
        printEmptyLine(terminal);
        if(client.getPlayerNumber() != 0) {
            printTerminalCenteredLine(terminal, "Players number: " + client.getPlayerNumber());
            printEmptyLine(terminal);
            printTerminalCenteredLine(terminal, "Game mode: " + (client.isAdvancedGame() ? GameMode.COMPLETE : GameMode.EASY));
            printEmptyLine(terminal);
        }
        printEmptyLine(terminal);
        String justConnected = waitForConnectedOrStart();
        while (justConnected != null) {
            printTerminalCenteredLine(terminal, "New user connected: " + justConnected + "\n");
            justConnected = waitForConnectedOrStart();
        }
        onEnd();
    }
}

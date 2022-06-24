package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractLobbySelectionPages;

import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;
import static org.fusesource.jansi.Ansi.ansi;

public class LobbySelectionPageCLI extends AbstractLobbySelectionPages {
    public LobbySelectionPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        final String LOBBY = """
                 ██████╗ ██████╗ ███████╗███╗   ██╗    ██╗      ██████╗ ██████╗ ██████╗ ██╗███████╗███████╗
                ██╔═══██╗██╔══██╗██╔════╝████╗  ██║    ██║     ██╔═══██╗██╔══██╗██╔══██╗██║██╔════╝██╔════╝
                ██║   ██║██████╔╝█████╗  ██╔██╗ ██║    ██║     ██║   ██║██████╔╝██████╔╝██║█████╗  ███████╗
                ██║   ██║██╔═══╝ ██╔══╝  ██║╚██╗██║    ██║     ██║   ██║██╔══██╗██╔══██╗██║██╔══╝  ╚════██║
                ╚██████╔╝██║     ███████╗██║ ╚████║    ███████╗╚██████╔╝██████╔╝██████╔╝██║███████╗███████║
                 ╚═════╝ ╚═╝     ╚══════╝╚═╝  ╚═══╝    ╚══════╝ ╚═════╝ ╚═════╝ ╚═════╝ ╚═╝╚══════╝╚══════╝
                """;
        final int lobbyWidth = 18;
        final int lobbyHeight = 9;
        final int baseRow = 12;

        String lobbyCode = null;
        do {
            int lobbiesPerRow = Integer.min(client.getLobbies().size(), 8);
            int baseCol = (terminal.getWidth() - lobbiesPerRow * (lobbyWidth + 2)) / 2;

            // print heading
            clearTerminal(terminal);
            printTerminalCenteredMultilineText(terminal, LOBBY, 40);
            if (lobbyCode != null && !lobbyCode.equals("#---")) { // not first run
                printTopErrorBanner(terminal, "The lobby you selected was either full or not existent!");
            }

            // print lobbies
            for (int i = 0; i < client.getLobbies().size(); i++) {
                List<Integer> base = Arrays.asList(
                        baseRow + (i / lobbiesPerRow) * (lobbyHeight + 1),  // base row
                        baseCol + (i % lobbiesPerRow) * (lobbyWidth + 2)    // base col
                );
                printLobby(terminal, base, client.getLobbies().get(i));
            }
            terminal.writer().print(ansi().cursorDownLine(3));

            // select lobby to join
            lobbyCode = readLobbyCode(terminal, "Insert the code of the lobby you wish to join (for example #N3m, to refresh the list just type 'r'):", client.getLobbies());
            if (lobbyCode.equals("r")) {
                lobbyCode = "#---";
            }
        } while (!tryToJoinLobby(lobbyCode));

        onEnd();
    }
}
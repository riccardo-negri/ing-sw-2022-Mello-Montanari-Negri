package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMatchmakingPage;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class MatchmakingPageCLI extends AbstractMatchmakingPage {
    public MatchmakingPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        String GAME_MODE =
                "███╗   ███╗ █████╗ ████████╗ ██████╗██╗  ██╗███╗   ███╗ █████╗ ██╗  ██╗██╗███╗   ██╗ ██████╗ \n" +
                        "████╗ ████║██╔══██╗╚══██╔══╝██╔════╝██║  ██║████╗ ████║██╔══██╗██║ ██╔╝██║████╗  ██║██╔════╝ \n" +
                        "██╔████╔██║███████║   ██║   ██║     ███████║██╔████╔██║███████║█████╔╝ ██║██╔██╗ ██║██║  ███╗\n" +
                        "██║╚██╔╝██║██╔══██║   ██║   ██║     ██╔══██║██║╚██╔╝██║██╔══██║██╔═██╗ ██║██║╚██╗██║██║   ██║\n" +
                        "██║ ╚═╝ ██║██║  ██║   ██║   ╚██████╗██║  ██║██║ ╚═╝ ██║██║  ██║██║  ██╗██║██║ ╚████║╚██████╔╝\n" +
                        "╚═╝     ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝ \n";

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, GAME_MODE, 1);
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "You are currently in queue to join a game...");
        onEnd();
    }
}

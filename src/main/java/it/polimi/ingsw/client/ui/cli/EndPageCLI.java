package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractEndPage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class EndPageCLI extends AbstractEndPage {

    public EndPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        String YOU_WON =
                "██╗   ██╗ ██████╗ ██╗   ██╗    ██╗    ██╗ ██████╗ ███╗   ██╗\n" +
                        "╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║    ██║██╔═══██╗████╗  ██║\n" +
                        " ╚████╔╝ ██║   ██║██║   ██║    ██║ █╗ ██║██║   ██║██╔██╗ ██║\n" +
                        "  ╚██╔╝  ██║   ██║██║   ██║    ██║███╗██║██║   ██║██║╚██╗██║\n" +
                        "   ██║   ╚██████╔╝╚██████╔╝    ╚███╔███╔╝╚██████╔╝██║ ╚████║\n" +
                        "   ╚═╝    ╚═════╝  ╚═════╝      ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═══╝\n";
        String YOU_LOST =
                "██╗   ██╗ ██████╗ ██╗   ██╗    ██╗      ██████╗ ███████╗████████╗\n" +
                        "╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║     ██╔═══██╗██╔════╝╚══██╔══╝\n" +
                        " ╚████╔╝ ██║   ██║██║   ██║    ██║     ██║   ██║███████╗   ██║   \n" +
                        "  ╚██╔╝  ██║   ██║██║   ██║    ██║     ██║   ██║╚════██║   ██║   \n" +
                        "   ██║   ╚██████╔╝╚██████╔╝    ███████╗╚██████╔╝███████║   ██║   \n" +
                        "   ╚═╝    ╚═════╝  ╚═════╝     ╚══════╝ ╚═════╝ ╚══════╝   ╚═╝   \n";
        clearTerminal(terminal);

        if (model.getWinner() == model.getWizard(client.getUsernames().indexOf(client.getUsername())).getTowerColor()) {
            printTerminalCenteredMultilineText(terminal, YOU_WON + "\nPress enter to continue...");
        }
        else {
            printTerminalCenteredMultilineText(terminal, YOU_LOST + "\nPress enter to continue...");
        }
        moveCursorToEnd(terminal);
        waitEnterPressed(terminal);
        onEnd();
    }
}

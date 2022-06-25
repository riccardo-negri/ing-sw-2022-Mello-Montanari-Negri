package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractEndPage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class EndPageCLI extends AbstractEndPage {

    public EndPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw () {
        final String YOU_WON = """
                ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗    ██╗ ██████╗ ███╗   ██╗
                ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║    ██║██╔═══██╗████╗  ██║
                 ╚████╔╝ ██║   ██║██║   ██║    ██║ █╗ ██║██║   ██║██╔██╗ ██║
                  ╚██╔╝  ██║   ██║██║   ██║    ██║███╗██║██║   ██║██║╚██╗██║
                   ██║   ╚██████╔╝╚██████╔╝    ╚███╔███╔╝╚██████╔╝██║ ╚████║
                   ╚═╝    ╚═════╝  ╚═════╝      ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═══╝
                """;

        final String YOU_LOST = """
                ██╗   ██╗ ██████╗ ██╗   ██╗    ██╗      ██████╗ ███████╗████████╗
                ╚██╗ ██╔╝██╔═══██╗██║   ██║    ██║     ██╔═══██╗██╔════╝╚══██╔══╝
                 ╚████╔╝ ██║   ██║██║   ██║    ██║     ██║   ██║███████╗   ██║\040\040\040
                  ╚██╔╝  ██║   ██║██║   ██║    ██║     ██║   ██║╚════██║   ██║\040\040\040
                   ██║   ╚██████╔╝╚██████╔╝    ███████╗╚██████╔╝███████║   ██║\040\040\040
                   ╚═╝    ╚═════╝  ╚═════╝     ╚══════╝ ╚═════╝ ╚══════╝   ╚═╝\040\040\040
                """;

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

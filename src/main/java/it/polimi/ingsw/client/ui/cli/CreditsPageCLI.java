package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractCreditsPage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class CreditsPageCLI extends AbstractCreditsPage {

    public CreditsPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw () {
        final String CREDITS = """
                  ██████╗██████╗ ███████╗██████╗ ██╗████████╗███████╗
                 ██╔════╝██╔══██╗██╔════╝██╔══██╗██║╚══██╔══╝██╔════╝
                 ██║     ██████╔╝█████╗  ██║  ██║██║   ██║   ███████╗
                 ██║     ██╔══██╗██╔══╝  ██║  ██║██║   ██║   ╚════██║
                 ╚██████╗██║  ██║███████╗██████╔╝██║   ██║   ███████║
                   ╚═════╝╚═╝  ╚═╝╚══════╝╚═════╝ ╚═╝   ╚═╝   ╚══════╝\040
                """;

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, CREDITS + "\nGame Design: Leo Colovini\nDevelopment: Simone Luciani \nArt: Alessandro Costa Kapakkione \nGraphics: Elisabetta Micucci \nRules: David Digby \nEdition: Giuliano Acquati and Lorenzo Tucci Sorrentino\n" + "\nPress enter to continue...");
        moveCursorToEnd(terminal);
        waitEnterPressed(terminal);
        onEnd();
    }
}

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMenuPage;

import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class MenuPageCLI extends AbstractMenuPage {

    public MenuPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        final String MENU = """
                ███╗   ███╗███████╗███╗   ██╗██╗   ██╗
                ████╗ ████║██╔════╝████╗  ██║██║   ██║
                ██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║
                ██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║
                ██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝
                ╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝\040
                """;

        final String CONTENT = MENU + "\n1) Play the Game" + "\n2) Storyline" + "\n 3) Credits " + "\n 4) Leave the Game\n";

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, CONTENT);
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "Please select your next page:", 1);
        AtomicReference<Integer> selection = new AtomicReference<>(readNumber(terminal));
        while (selection.get() < 0 || selection.get() > 4) {
            clearTerminal(terminal);
            printTerminalCenteredMultilineText(terminal, CONTENT);
            printTopErrorBanner(terminal, "Please type a correct option!");
            printTerminalCenteredLine(terminal, "Please select your next page:", 1);
            selection.set(readNumber(terminal));
        }
        if (selection.get() == 4) {
            clearTerminal(terminal);
            moveCursorToTop(terminal);
        }
        onEnd(selection.get());
    }
}

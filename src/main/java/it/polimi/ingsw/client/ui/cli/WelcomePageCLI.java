package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

/**
 * CLI implementation of the WelcomePage
 */
public class WelcomePageCLI extends AbstractWelcomePage {

    public WelcomePageCLI (Client client) {
        super(client);
    }

    /**
     * draw the page and handle all expected actions
     */
    @Override
    public void draw () {
        final String WELCOME_TO = """
                ██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗\040
                ██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗
                ██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║
                ██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║
                ╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝
                 ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝\040
                """;
        final String ERIANTYS = """
                ███████╗██████╗ ██╗ █████╗ ███╗   ██╗████████╗██╗   ██╗███████╗
                ██╔════╝██╔══██╗██║██╔══██╗████╗  ██║╚══██╔══╝╚██╗ ██╔╝██╔════╝
                █████╗  ██████╔╝██║███████║██╔██╗ ██║   ██║    ╚████╔╝ ███████╗
                ██╔══╝  ██╔══██╗██║██╔══██║██║╚██╗██║   ██║     ╚██╔╝  ╚════██║
                ███████╗██║  ██║██║██║  ██║██║ ╚████║   ██║      ██║   ███████║
                ╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝      ╚═╝   ╚══════╝
                """;
        final String SMALL_MONITORS = """              
                ███████╗███╗   ███╗ █████╗ ██╗     ██╗         ███╗   ███╗ ██████╗ ███╗   ██╗██╗████████╗ ██████╗ ██████╗ ███████╗
                ██╔════╝████╗ ████║██╔══██╗██║     ██║         ████╗ ████║██╔═══██╗████╗  ██║██║╚══██╔══╝██╔═══██╗██╔══██╗██╔════╝
                ███████╗██╔████╔██║███████║██║     ██║         ██╔████╔██║██║   ██║██╔██╗ ██║██║   ██║   ██║   ██║██████╔╝███████╗
                ╚════██║██║╚██╔╝██║██╔══██║██║     ██║         ██║╚██╔╝██║██║   ██║██║╚██╗██║██║   ██║   ██║   ██║██╔══██╗╚════██║
                ███████║██║ ╚═╝ ██║██║  ██║███████╗███████╗    ██║ ╚═╝ ██║╚██████╔╝██║ ╚████║██║   ██║   ╚██████╔╝██║  ██║███████║
                ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝    ╚═╝     ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚══════╝
                """;
        final String ARE_NOT_SUPPORTED = """
                 █████╗ ██████╗ ███████╗    ███╗   ██╗ ██████╗ ████████╗    ███████╗██╗   ██╗██████╗ ██████╗  ██████╗ ██████╗ ████████╗███████╗██████╗
                ██╔══██╗██╔══██╗██╔════╝    ████╗  ██║██╔═══██╗╚══██╔══╝    ██╔════╝██║   ██║██╔══██╗██╔══██╗██╔═══██╗██╔══██╗╚══██╔══╝██╔════╝██╔══██╗
                ███████║██████╔╝█████╗      ██╔██╗ ██║██║   ██║   ██║       ███████╗██║   ██║██████╔╝██████╔╝██║   ██║██████╔╝   ██║   █████╗  ██║  ██║
                ██╔══██║██╔══██╗██╔══╝      ██║╚██╗██║██║   ██║   ██║       ╚════██║██║   ██║██╔═══╝ ██╔═══╝ ██║   ██║██╔══██╗   ██║   ██╔══╝  ██║  ██║
                ██║  ██║██║  ██║███████╗    ██║ ╚████║╚██████╔╝   ██║       ███████║╚██████╔╝██║     ██║     ╚██████╔╝██║  ██║   ██║   ███████╗██████╔╝
                ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝    ╚═╝  ╚═══╝ ╚═════╝    ╚═╝       ╚══════╝ ╚═════╝ ╚═╝     ╚═╝      ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═════╝\040
                """;

        clearTerminal(terminal);
        if (terminal.getWidth() < 200) {
            printTerminalCenteredMultilineText(terminal, SMALL_MONITORS + "\n" + ARE_NOT_SUPPORTED + "\nTry to reduce the font size of your terminal.\nPress enter to quit the game...");
            moveCursorToEnd(terminal);
            waitEnterPressed(terminal);
            onEnd(false);
        }
        else {
            printTerminalCenteredMultilineText(terminal, WELCOME_TO + "\n" + ERIANTYS + "\nMade by Pietro Mello Rella, Tommaso Montanari and Riccardo Negri\n" + "\nPress enter to continue...");
            moveCursorToEnd(terminal);
            waitEnterPressed(terminal);
            onEnd(true);
        }

    }
}

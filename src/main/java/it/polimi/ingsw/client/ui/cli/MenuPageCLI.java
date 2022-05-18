package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractMenuPage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class MenuPageCLI extends AbstractMenuPage {

    public MenuPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        String MENU =
                "███╗   ███╗███████╗███╗   ██╗██╗   ██╗\n" +
                        "████╗ ████║██╔════╝████╗  ██║██║   ██║\n" +
                        "██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║\n" +
                        "██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║\n" +
                        "██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝\n" +
                        "╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝ \n";
        String CONTENT = MENU + "\n1) Play the Game" + "\n2) Storyline" + "\n 3) Credits " +"\n 4) Leave the Game\n";

        clearTerminal(terminal);
        printTerminalCenteredMultilineText(terminal, CONTENT);
        printEmptyLine(terminal);
        printTerminalCenteredLine(terminal, "Please select your next page:", 1);
        Integer temp = readNumber(terminal);
        while (temp < 0 || temp > 4) {
            clearTerminal(terminal);
            printTerminalCenteredMultilineText(terminal, CONTENT);
            printTopErrorBanner(terminal, "Please type a correct option!");
            printTerminalCenteredLine(terminal, "Please select your next page:", 1);
            temp = readNumber(terminal);
        }
        if(temp == 4) {
            clearTerminal(terminal);
            moveCursorToTop(terminal);
        }
        onEnd(temp);
    }
}

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractStartPage;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class StartPageCLI extends AbstractStartPage {

    public StartPageCLI (Client client) {
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
        String CONTENT = MENU + "\n1) Start the Game" + "\n2) Settings" + "\n 3) Credits and Storyline\n";

        clearTerminal();
        printTerminalCenteredMultilineText(CONTENT);
        printEmptyLine();
        printTerminalCenteredLine("Please select your next page:", 1);
        Integer temp = readNumber();
        while(temp<0 || temp>3) {
            clearTerminal();
            printTerminalCenteredMultilineText(CONTENT);
            printTopErrorBanner("Please type a correct option!");
            printTerminalCenteredLine("Please select your next page:", 1);
            temp = readNumber();
        }
        onEnd(temp);
    }
}

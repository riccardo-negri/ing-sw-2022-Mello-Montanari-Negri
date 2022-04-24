package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractStartPage;

public class StartPageCLI extends AbstractStartPage {

    public StartPageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        CoreCLI cli = (CoreCLI) client.getUI();
        String MENU =
                "███╗   ███╗███████╗███╗   ██╗██╗   ██╗\n" +
                "████╗ ████║██╔════╝████╗  ██║██║   ██║\n" +
                "██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║\n" +
                "██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║\n" +
                "██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝\n" +
                "╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝ \n";
        String CONTENT = MENU + "\n1) Start the Game" + "\n2) Settings" + "\n 3) Credits and Storyline\n";

        cli.clearTerminal();
        cli.printTerminalCenteredMultilineText(CONTENT);
        cli.printEmptyLine();
        cli.printTerminalCenteredLine("Please select your next page:", 1);
        Integer temp = cli.readNumber();
        while(temp<0 || temp>3) {
            cli.clearTerminal();
            cli.printTerminalCenteredMultilineText(CONTENT);
            cli.printTopErrorBanner("Please type a correct option!");
            cli.printTerminalCenteredLine("Please select your next page:", 1);
            temp = cli.readNumber();
        }
        onEnd(temp);
    }
}

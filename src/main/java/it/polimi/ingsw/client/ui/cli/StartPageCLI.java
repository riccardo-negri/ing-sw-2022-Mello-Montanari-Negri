package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.pages.AbstractStartPage;

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
        String CONTENT = MENU + "\n1) Start the Game" + "\n2) Settings" + "\n3) Credits and Storyline\n" + "\nPlease select an option...";

        cli.printTerminalCenteredMultilineText(CONTENT);
        Integer temp = cli.readNumber();
        while(temp<0 || temp>3) {
            cli.printTerminalCenteredMultilineText(CONTENT);
            cli.printTopErrorBanner("Please type a correct option!");
            temp = cli.readNumber();
        }
        onEnd(temp);
    }
}

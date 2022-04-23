package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.states.AbstractStartPageClientState;

public class StartPageClientStateCLI extends AbstractStartPageClientState {
    private CoreCLI cli;
    private final String MENU =
            "███╗   ███╗███████╗███╗   ██╗██╗   ██╗\n" +
            "████╗ ████║██╔════╝████╗  ██║██║   ██║\n" +
            "██╔████╔██║█████╗  ██╔██╗ ██║██║   ██║\n" +
            "██║╚██╔╝██║██╔══╝  ██║╚██╗██║██║   ██║\n" +
            "██║ ╚═╝ ██║███████╗██║ ╚████║╚██████╔╝\n" +
            "╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝ ╚═════╝ \n";
    private final String CONTENT = MENU + "\n1) Start the Game" + "\n2) Settings" + "\n3) Credits and Storyline\n" + "\nPlease select an option...";

    public StartPageClientStateCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        Integer temp;
        cli = (CoreCLI) client.getUI();
        cli.printTerminalCenteredText(CONTENT);
        temp = cli.readNumber();
        while(temp<0 || temp>3) {
            cli.printTerminalCenteredText(CONTENT);
            cli.printTopErrorBanner("Please type a correct option!");
            temp = cli.readNumber();
        }
        onEnd();
    }
}

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.states.AbstractWelcomePageClientState;

public class WelcomePageClientStateCLI extends AbstractWelcomePageClientState {
    private CoreCLI cli;
    // ANSI Shadow (https://patorjk.com/software/taag/#p=testall&h=2&f=Avatar&t=ERIANTYS)
    private final String WELCOME_TO =
                    "██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗ \n" +
                    "██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗\n" +
                    "██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║\n" +
                    "██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║\n" +
                    "╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝\n" +
                    " ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝ \n";

    private final String ERIANTYS =
                    "███████╗██████╗ ██╗ █████╗ ███╗   ██╗████████╗██╗   ██╗███████╗\n" +
                    "██╔════╝██╔══██╗██║██╔══██╗████╗  ██║╚══██╔══╝╚██╗ ██╔╝██╔════╝\n" +
                    "█████╗  ██████╔╝██║███████║██╔██╗ ██║   ██║    ╚████╔╝ ███████╗\n" +
                    "██╔══╝  ██╔══██╗██║██╔══██║██║╚██╗██║   ██║     ╚██╔╝  ╚════██║\n" +
                    "███████╗██║  ██║██║██║  ██║██║ ╚████║   ██║      ██║   ███████║\n" +
                    "╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝      ╚═╝   ╚══════╝\n";

    public WelcomePageClientStateCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        cli = (CoreCLI) client.getUI();
        cli.printTerminalCenteredText(WELCOME_TO + "\n" + ERIANTYS + "\nMade by Pietro Mello Rella, Tommaso Montanari and Riccardo Negri\n" + "\nPress enter to continue...");
        cli.waitKeyPressed();
        onEnd();
    }
}

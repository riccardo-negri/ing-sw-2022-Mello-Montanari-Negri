package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class WelcomePageCLI extends AbstractWelcomePage {

    public WelcomePageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        //Terminal terminal = cli.getTerminal();
        // ANSI Shadow (https://patorjk.com/software/taag/#p=testall&h=2&f=Avatar&t=ERIANTYS)
        String WELCOME_TO =
                        "██╗    ██╗███████╗██╗      ██████╗ ██████╗ ███╗   ███╗███████╗    ████████╗ ██████╗ \n" +
                        "██║    ██║██╔════╝██║     ██╔════╝██╔═══██╗████╗ ████║██╔════╝    ╚══██╔══╝██╔═══██╗\n" +
                        "██║ █╗ ██║█████╗  ██║     ██║     ██║   ██║██╔████╔██║█████╗         ██║   ██║   ██║\n" +
                        "██║███╗██║██╔══╝  ██║     ██║     ██║   ██║██║╚██╔╝██║██╔══╝         ██║   ██║   ██║\n" +
                        "╚███╔███╔╝███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║███████╗       ██║   ╚██████╔╝\n" +
                        " ╚══╝╚══╝ ╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝       ╚═╝    ╚═════╝ \n";
        String ERIANTYS =
                        "███████╗██████╗ ██╗ █████╗ ███╗   ██╗████████╗██╗   ██╗███████╗\n" +
                        "██╔════╝██╔══██╗██║██╔══██╗████╗  ██║╚══██╔══╝╚██╗ ██╔╝██╔════╝\n" +
                        "█████╗  ██████╔╝██║███████║██╔██╗ ██║   ██║    ╚████╔╝ ███████╗\n" +
                        "██╔══╝  ██╔══██╗██║██╔══██║██║╚██╗██║   ██║     ╚██╔╝  ╚════██║\n" +
                        "███████╗██║  ██║██║██║  ██║██║ ╚████║   ██║      ██║   ███████║\n" +
                        "╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝      ╚═╝   ╚══════╝\n";
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
                ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝    ╚═╝  ╚═══╝ ╚═════╝    ╚═╝       ╚══════╝ ╚═════╝ ╚═╝     ╚═╝      ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═════╝
                \s                      
                """;
        // https://www.tabnine.com/code/query/%22JLINE%22+org.fusesource.jansi@Ansi
        // https://github.com/jline/jline3/wiki/Completion
        // https://github.com/jline/jline3/wiki/Autosuggestions
        clearTerminal(terminal);
        if (terminal.getWidth() < 200) {
            printTerminalCenteredMultilineText(terminal, SMALL_MONITORS + "\n" + ARE_NOT_SUPPORTED +  "Try to reduce the font size of your terminal.\nPress enter to quit the game...");
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

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractWelcomePage;
import org.jline.builtins.Completers;
import org.jline.console.impl.JlineCommandRegistry;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;

import java.util.Arrays;

import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class WelcomePageCLI extends AbstractWelcomePage {

    public WelcomePageCLI (Client client) {
        super(client);
    }

    @Override
    public void draw (Client client) {
        CLI cli = (CLI) client.getUI();
        Terminal terminal = cli.getTerminal();
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
        //clearTerminal();

        // https://www.tabnine.com/code/query/%22JLINE%22+org.fusesource.jansi@Ansi
        // https://github.com/jline/jline3/wiki/Completion
        // https://github.com/jline/jline3/wiki/Autosuggestions
        LineReader reader = null;
        reader = LineReaderBuilder.builder()
                .completer(new ArgumentCompleter(
                        new StringsCompleter("movee"),
                        new Completers.OptionCompleter(Arrays.asList(
                                new StringsCompleter("student"),
                                new StringsCompleter("green", "red", "yellow", "pink", "blue"),
                                new StringsCompleter("island"),
                                new StringsCompleter("1", "2", "3", "4", "5"), NullCompleter.INSTANCE), JlineCommandRegistry.compileCommandOptions(""), 1)))
                .parser(new DefaultParser())
                .build();

        printTerminalCenteredMultilineText(reader, WELCOME_TO + "\n" + ERIANTYS + "\nMade by Pietro Mello Rella, Tommaso Montanari and Riccardo Negri\n" + "\nPress enter to continue...");
        //moveCursorToEnd();


        getMoveToIsland(reader);
        onEnd();
    }
}

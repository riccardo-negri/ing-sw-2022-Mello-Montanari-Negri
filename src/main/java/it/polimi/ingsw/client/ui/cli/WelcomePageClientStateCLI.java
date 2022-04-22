package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.states.AbstractWelcomePageClientState;

import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

public class WelcomePageClientStateCLI extends AbstractWelcomePageClientState {
    public final String ANSI_CLS = "\u001b[2J";
    public final String ANSI_HOME = "\u001b[H";
    public final String ANSI_BOLD = "\u001b[1m";
    public final String ANSI_AT55 = "\u001b[10;10H";
    public final String ANSI_REVERSEON = "\u001b[7m";
    public final String ANSI_NORMAL = "\u001b[0m";
    public final String ANSI_WHITEONBLUE = "\u001b[37;44m";

    // ANSI Shadow (https://patorjk.com/software/taag/#p=testall&h=2&f=Avatar&t=ERIANTYS)
    public final String MAYBE_WORKS =
            "███████╗██████╗ ██╗ █████╗ ███╗   ██╗████████╗██╗   ██╗███████╗\n" +
            "██╔════╝██╔══██╗██║██╔══██╗████╗  ██║╚══██╔══╝╚██╗ ██╔╝██╔════╝\n" +
            "█████╗  ██████╔╝██║███████║██╔██╗ ██║   ██║    ╚████╔╝ ███████╗\n" +
            "██╔══╝  ██╔══██╗██║██╔══██║██║╚██╗██║   ██║     ╚██╔╝  ╚════██║\n" +
            "███████╗██║  ██║██║██║  ██║██║ ╚████║   ██║      ██║   ███████║\n" +
            "╚══════╝╚═╝  ╚═╝╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝      ╚═╝   ╚══════╝\n";
    @Override
    public void draw () {
        System.out.println("Test");
        AnsiConsole.systemInstall();
        System.out.println(ansi().reset().eraseScreen());
        System.out.println(ansi().fgBrightBlue().cursor(20,0).a(MAYBE_WORKS));
        try {System.in.read();}catch(Exception e){};
        AnsiConsole.out.println(ANSI_CLS);
        AnsiConsole.out.println(ANSI_AT55 + ANSI_REVERSEON + "Hello world" + ANSI_NORMAL);
        AnsiConsole.out.println(ANSI_CLS);
        AnsiConsole.out.println(ANSI_HOME + ANSI_WHITEONBLUE + "Hello woorld" + ANSI_NORMAL);
        AnsiConsole.out.print(ANSI_BOLD + "Press a key..." + ANSI_NORMAL);
        try {System.in.read();}catch(Exception e){}
        AnsiConsole.out.println(ANSI_CLS);
        //System.out.println(ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset());
        //System.out.println(ansi().eraseScreen().fg(RED).bg(YELLOW).a("Welcome").fg(GREEN).a("Here").reset());

    }
}

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.states.AbstractClientState;
import it.polimi.ingsw.client.states.ClientState;
import it.polimi.ingsw.client.ui.UI;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


public class CoreCLI implements UI {
    Scanner scanner = new Scanner(System.in);

    public void init () {
        AnsiConsole.systemInstall();
    }


    @Override
    public AbstractClientState getState (Client client, ClientState nextState) {
        switch(nextState) {
            case WELCOME_PAGE: return new WelcomePageClientStateCLI(client);
            case START_PAGE: return new StartPageClientStateCLI(client);
        }
        return null;
    }


    public Integer getTerminalWidth () {
        Integer terminalWidth;
        try {
            terminalWidth = org.jline.terminal.TerminalBuilder.terminal().getWidth();
            return terminalWidth;
        } catch (Exception ignored) {
            return 100;
        }
    }
    public Integer getTerminalHeight () {
        Integer terminalHeight;
        try {
            terminalHeight = org.jline.terminal.TerminalBuilder.terminal().getHeight();
            return terminalHeight;
        } catch (Exception ignored) {
            return 50;
        }

    }

    public void clearTerminal() {
        AnsiConsole.out().println(ansi().reset().eraseScreen());
    }

    public void printTerminalCenteredText(String s) {
        clearTerminal();
        String[] arr = s.split("\n");
        List<String> list = Arrays.asList(arr);

        //Integer width = Arrays.stream(arr).mapToInt(String::length).max().orElse(50);
        Integer startH = getTerminalHeight()/2 - list.size()/2;

        IntStream.range(0, list.size()).forEach(ind ->
                AnsiConsole.out().println(
                        ansi()
                                .cursor(
                                        startH+ind,
                                        getTerminalWidth()/2-list.get(ind).length()/2
                                )
                                .a(
                                        list.get(ind)
                                )
                )
        );

        AnsiConsole.out().println(ansi().cursor(1000,1000));
    }

    public void waitKeyPressed() {
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    public Integer readNumber() {
        Integer num;
        try {
            num = Integer.valueOf(scanner.nextLine());
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void printTopErrorBanner(String warning) {
        AnsiConsole.out().println(
                ansi().fgBrightRed() .
                cursor(
                                2,
                                getTerminalWidth()/2-warning.length()/2
                        )
                        .a(
                                warning
                        )

        );
        AnsiConsole.out().println(ansi().cursor(1000,1000).fg(Ansi.Color.WHITE));
    }

}

package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import org.jline.builtins.Completers;
import org.jline.console.impl.JlineCommandRegistry;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class CLI implements UI {
    Scanner scanner = new Scanner(System.in);
    private Terminal terminal;

    public void init () {

        // https://superuser.com/questions/413073/windows-console-with-ansi-colors-handling
        // https://github.com/jline/jline3/issues/779
        //AnsiConsole.systemInstall();
        try {
            terminal = TerminalBuilder.terminal();
            System.out.println(terminal.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractClientState getState (Client client, ClientState nextState) {
        switch (nextState) {
            case WELCOME_PAGE:
                return new WelcomePageCLI(client);
            case START_PAGE:
                return new StartPageCLI(client);
            case CONNECTION_PAGE:
                return new ConnectionPageCLI(client);
            case GAME_MODE_SELECTION_PAGE:
                return new GameModeSelectionPageCLI(client);
            case MATCHMAKING_PAGE:
                return new MatchmakingPageCLI(client);
            case BOARD_PAGE:
                return new BoardPageCLI(client);
        }
        return null;
    }

    public Terminal getTerminal () {
        return terminal;
    }

    public void setTerminal (Terminal terminal) {
        this.terminal = terminal;
    }
}
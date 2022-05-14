package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.client.ui.UI;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Scanner;

public class CLI implements UI {
    Scanner scanner = new Scanner(System.in);
    private Terminal terminal;
    private History commandsHistory;

    public void init () {

        // https://superuser.com/questions/413073/windows-console-with-ansi-colors-handling
        // https://github.com/jline/jline3/issues/779
        //AnsiConsole.systemInstall();
        try {
            terminal = TerminalBuilder.terminal();
            commandsHistory = new DefaultHistory();
            commandsHistory.add("play assistant 1");
            System.out.println(terminal.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractPage getState (Client client, ClientPage nextState) {
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

    public History getCommandsHistory () {
        return commandsHistory;
    }

    public void setTerminal (Terminal terminal) {
        this.terminal = terminal;
    }
}
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

public class CLI implements UI {
    private Terminal terminal;
    private History commandsHistory;

    public void init () {

        // https://superuser.com/questions/413073/windows-console-with-ansi-colors-handling
        // https://github.com/jline/jline3/issues/779
        try {
            terminal = TerminalBuilder.terminal();
            commandsHistory = new DefaultHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AbstractPage getState (Client client, ClientPage nextState) {
        return switch (nextState) {
            case WELCOME_PAGE -> new WelcomePageCLI(client);
            case START_PAGE -> new StartPageCLI(client);
            case CONNECTION_PAGE -> new ConnectionPageCLI(client);
            case GAME_MODE_SELECTION_PAGE -> new GameModeSelectionPageCLI(client);
            case MATCHMAKING_PAGE -> new MatchmakingPageCLI(client);
            case BOARD_PAGE -> new BoardPageCLI(client);
        };
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
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

/**
 * class that represents the CLI
 */
public class CLI implements UI {
    private Terminal terminal;
    private History commandsHistory;

    /**
     * CLI initializer that initializes JLine terminal and commands history
     */
    public void init () {
        try {
            terminal = TerminalBuilder.terminal();
            commandsHistory = new DefaultHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get CLI page based on state
     * @param client client
     * @param nextState state to create the page from
     * @return CLI page
     */
    @Override
    public AbstractPage getState (Client client, ClientPage nextState) {
        return switch (nextState) {
            case WELCOME_PAGE -> new WelcomePageCLI(client);
            case MENU_PAGE -> new MenuPageCLI(client);
            case STORYLINE_PAGE -> new StorylinePageCLI(client);
            case CREDITS_PAGE -> new CreditsPageCLI(client);
            case CONNECTION_PAGE -> new ConnectionPageCLI(client);
            case CREATE_GAME_PAGE -> new CreateGamePageCLI(client);
            case LOBBY_SELECTION_PAGE -> new LobbySelectionPageCLI(client);
            case LOBBY_PAGE -> new LobbyPageCLI(client);
            case BOARD_PAGE -> new BoardPageCLI(client);
            case END_PAGE -> new EndPageCLI(client);
        };
    }

    public Terminal getTerminal () {
        return terminal;
    }

    public History getCommandsHistory () {
        return commandsHistory;
    }

}
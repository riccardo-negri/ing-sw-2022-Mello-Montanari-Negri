package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractClientState;
import it.polimi.ingsw.client.page.ClientState;
import it.polimi.ingsw.client.ui.UI;
import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class CLI implements UI {
    Scanner scanner = new Scanner(System.in);

    public void init () {
        // https://superuser.com/questions/413073/windows-console-with-ansi-colors-handling
        // https://github.com/jline/jline3/issues/779
        AnsiConsole.systemInstall();
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
}
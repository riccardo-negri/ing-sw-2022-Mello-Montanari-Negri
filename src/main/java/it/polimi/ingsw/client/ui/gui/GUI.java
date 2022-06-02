package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.*;

public class GUI implements UI {
    @Override
    public AbstractPage getState (Client client, ClientPage nextState) {
        return switch (nextState) {
            case WELCOME_PAGE -> new WelcomePageGUI(client);
            case MENU_PAGE -> null;
            case STORYLINE_PAGE -> null;
            case CREDITS_PAGE -> null;
            case CONNECTION_PAGE -> null;
            case CREATE_GAME_PAGE -> null;
            case LOBBY_SELECTION_PAGE -> null;
            case LOBBY_PAGE -> null;
            case BOARD_PAGE -> null;
            case END_PAGE -> null;
        };
    }
}

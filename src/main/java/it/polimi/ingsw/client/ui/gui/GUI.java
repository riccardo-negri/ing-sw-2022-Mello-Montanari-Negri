package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.client.ui.UI;
import it.polimi.ingsw.client.ui.cli.*;
import javafx.stage.Stage;

public class GUI implements UI {

    Stage stage;

    public GUI(Stage stage) {
        this.stage = stage;
    }

    @Override
    public AbstractPage getState (Client client, ClientPage nextState) {
        return switch (nextState) {
            case WELCOME_PAGE -> new WelcomePageGUI(client);
            case MENU_PAGE -> new MenuPageGUI(client);
            case STORYLINE_PAGE -> null;
            case CREDITS_PAGE -> null;
            case CONNECTION_PAGE -> new ConnectionPageGUI(client);
            case CREATE_GAME_PAGE -> new CreateGamePageGUI(client);
            case LOBBY_SELECTION_PAGE -> null;
            case LOBBY_PAGE -> new LobbyPageGUI(client);
            case BOARD_PAGE -> null;
            case END_PAGE -> null;
        };
    }

    public Stage getStage() {
        return stage;
    }
}

package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.client.ui.UI;
import javafx.stage.Stage;

public record GUI(Stage stage) implements UI {

    /**
     * map client states to graphic pages to show
     * @param client the client that is showing the pages
     * @param nextState the next state of the client (corresponds to the abstract page is going to)
     * @return the graphic page the client needs to show
     */
    @Override
    public AbstractPage getState(Client client, ClientPage nextState) {
        return switch (nextState) {
            case WELCOME_PAGE -> new WelcomePageGUI(client);
            case MENU_PAGE -> new MenuPageGUI(client);
            case STORYLINE_PAGE -> new StorylinePageGUI(client);
            case CREDITS_PAGE -> new CreditsPageGUI(client);
            case CONNECTION_PAGE -> new ConnectionPageGUI(client);
            case CREATE_GAME_PAGE -> new CreateGamePageGUI(client);
            case LOBBY_SELECTION_PAGE -> new LobbySelectionPageGUI(client);
            case LOBBY_PAGE -> new LobbyPageGUI(client);
            case BOARD_PAGE -> new BoardPageGUI(client);
            case END_PAGE -> new EndPageGUI(client);
        };
    }
}

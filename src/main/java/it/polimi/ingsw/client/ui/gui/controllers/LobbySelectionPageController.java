package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractLobbySelectionPages;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.networking.LobbyDescriptor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.util.Date;


public class LobbySelectionPageController extends AbstractController{
    @FXML
    ListView<String> lobbies;

    long lastClickTime = 0;
    String lastClickElement = null;
    private static final long DOUBLE_CLICK_TIME = 400;

    private static final String TITLE_ROW = "CODE\tN°\tMODE\t\tPLAYERS";

    /**
     * draw the content of the table
     */
    @FXML
    void initialize() {
        drawList();
    }

    /**
     * empty the table and add a new row for each available lobby
     */
    void drawList() {
        if (client.getLobbies() != null) {
            lobbies.getItems().clear();
            lobbies.getItems().add(TITLE_ROW);
            for (LobbyDescriptor ld : client.getLobbies()) {
                lobbies.getItems().add(formatLobby(ld));
            }
        }
    }

    /**
     * compare current time and selected element to previous click in order to detect a double click, in this case
     * join the selected lobby and go to lobby page
     */
    @FXML
    void handleJoin() {
        long currentTime = new Date().getTime();
        String row = lobbies.getSelectionModel().getSelectedItem();
        if (currentTime - lastClickTime <= DOUBLE_CLICK_TIME && !row.equals(TITLE_ROW) &&
                lastClickElement != null && lastClickElement.equals(row)) {
            String code = row.substring(0, 4);
            AbstractLobbySelectionPages page = (AbstractLobbySelectionPages) client.getCurrState();
            if(page.tryToJoinLobby(code)) {
                page.onEnd();
                Platform.runLater(()-> client.drawNextPage());
            }
            else {
                Platform.runLater(this::drawList);
            }
        }
        lastClickTime = currentTime;
        lastClickElement = row;
    }

    /**
     * send an invalid code to the server to get a new lobby list and use it to update the table content
     */
    @FXML
    void handleRefresh() {
        AbstractLobbySelectionPages page = (AbstractLobbySelectionPages) client.getCurrState();
        page.tryToJoinLobby("#");  // always wrong but the server will send the new list in response
        drawList();
    }

    /**
     * format the LobbyDescriptor data into a single string and keep consistent tabulations for any possible value
     * @param ld the LobbyDescriptor to convert to string
     * @return the correctly formatted string
     */
    String formatLobby(LobbyDescriptor ld) {
        StringBuilder record = new StringBuilder(ld.getCode() + "   " + "\t");
        record.append(ld.getPlayerNumber().getWizardNumber()).append("\t");
        record.append(formatMode(ld.getGameMode())).append("\t");
        for (String username: ld.getConnected()) {
            record.append(username).append(", ");
        }
        record.delete(record.length()-2, record.length()-1);
        return record.toString();
    }

    /**
     * convert game mode to string keeping same length for any value
     * @param gm the game mode to convert to string
     * @return the correctly formatted string
     */
    String formatMode(GameMode gm) {
        if(gm.equals(GameMode.EASY))
            return gm + "\t";
        else
            return gm.toString();
    }

}
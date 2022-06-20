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
    String lastClickElement = "";
    private static final long doubleClickTime = 400;

    @FXML
    void initialize() {
        drawList();
    }

    void drawList() {
        lobbies.getItems().clear();
        for (LobbyDescriptor ld : client.getLobbies()) {
            lobbies.getItems().add(formatLobby(ld));
        }
    }

    @FXML
    void handleJoin() {
        long currentTime = new Date().getTime();
        String row = lobbies.getSelectionModel().getSelectedItem();
        if(currentTime - lastClickTime <= doubleClickTime && lastClickElement.equals(row)) {
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

    String formatMode(GameMode gm) {
        if(gm.equals(GameMode.EASY))
            return gm + "\t";
        else
            return gm.toString();
    }

}
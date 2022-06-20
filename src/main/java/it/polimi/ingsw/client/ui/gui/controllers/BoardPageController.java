package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.Message;
import it.polimi.ingsw.networking.UserResigned;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BoardPageController extends AbstractController{


    @FXML
    private void handleResign(ActionEvent event) {
        client.getConnection().send(new UserResigned(client.getUsername()));
    }

    @FXML
    void initialize() {
        client.getConnection().bindFunctionAndTestPrevious(this::onNewMessage);
    }

    boolean onNewMessage(Connection source) {
        Message m = source.getFirstMessage();
        if (m instanceof UserResigned ur) {
            client.setResigned(ur.getUsername());
            ((AbstractBoardPage) client.getCurrState()).onEnd(false);
            client.getConnection().close();
            Platform.runLater(()-> client.drawNextPage());
            return true;
        }
        return false;
    }
}
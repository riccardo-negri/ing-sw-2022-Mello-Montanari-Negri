package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.page.AbstractEndPage;
import it.polimi.ingsw.networking.UserResigned;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BoardPageController extends AbstractController{


    @FXML
    private void handleResign(ActionEvent event) {
        client.getConnection().send(new UserResigned(client.getUsername()));
        ((AbstractBoardPage) client.getCurrState()).onEnd(false);
        client.drawNextPage();
    }
}
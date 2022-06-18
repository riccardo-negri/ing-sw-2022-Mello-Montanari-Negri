package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

import static it.polimi.ingsw.client.page.ClientPage.LOBBY_PAGE;

public class ConnectionPageController extends AbstractController{

    @FXML
    TextField username;
    @FXML
    TextField port;
    @FXML
    TextField ip;
    @FXML
    DialogPane dialog;
    @FXML
    private void handleConnect(ActionEvent event) {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        int p = Integer.parseInt(port.getText());
        page.connectToMatchmakingServer(ip.getText(), p, username.getText());
        page.waitForLobbiesListOrRedirect();

        if (client.getNextState() == LOBBY_PAGE) {
            client.drawNextPage();
        } else {
            dialog.setVisible(true);
            //boolean createGame = true;
            //page.onEnd(createGame);
        }
    }
}
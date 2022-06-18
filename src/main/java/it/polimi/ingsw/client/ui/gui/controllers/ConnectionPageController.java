package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractConnectionPage;
import it.polimi.ingsw.client.ui.gui.WelcomePageGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static it.polimi.ingsw.client.page.ClientPage.LOBBY_PAGE;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;

public class ConnectionPageController extends AbstractController{

    @FXML
    TextField username;
    @FXML
    TextField port;
    @FXML
    TextField ip;

    @FXML
    private void handleConnect(ActionEvent event) {
        AbstractConnectionPage page = (AbstractConnectionPage) client.getCurrState();
        int p = Integer.parseInt(port.getText());
        page.connectToMatchmakingServer(ip.getText(), p, username.getText());
        page.waitForLobbiesListOrRedirect();

        if (client.getNextState() != LOBBY_PAGE) {
            boolean createGame = true;
            page.onEnd(createGame);
        }
        client.drawNextPage();
    }
}
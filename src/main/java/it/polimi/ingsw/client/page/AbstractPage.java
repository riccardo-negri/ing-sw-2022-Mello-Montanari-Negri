package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.client.ui.gui.GUI;
import it.polimi.ingsw.client.ui.gui.controllers.AbstractController;
import it.polimi.ingsw.model.entity.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jline.reader.History;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class AbstractPage {
    protected Client client;
    protected CLI cli;
    protected Terminal terminal;
    protected Game model;
    protected Logger logger;
    protected History commandsHistory;

    protected AbstractPage (Client client) {
        this.client = client;
        model = client.getModel();
        logger = client.getLogger();

        if (client.getUI().getClass().equals(CLI.class)) {
            cli = (CLI) client.getUI();
            terminal = cli.getTerminal();
            commandsHistory = cli.getCommandsHistory();
        }

    }

    public abstract void draw (Client client); //TODO: remove useless parameter client, is present since creation class wide

    protected void showGUIPage(String title, String file) {
        Stage stage = ((GUI) client.getUI()).getStage();
        stage.setTitle(title);
        stage.setOnCloseRequest(t -> Platform.exit());  // stop process when window is closed


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AbstractController controller = loader.getController();
        controller.setClient(client);
        controller.setStage(stage);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}

package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.client.ui.gui.GUI;
import it.polimi.ingsw.client.ui.gui.controllers.AbstractController;
import it.polimi.ingsw.model.entity.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

    /**
     * draw the page and handle all expected actions
     */
    public abstract void draw ();

    /**
     * display new page on the stage
     * @param title window title
     * @param file FXML file of the page
     * @param controller page controller
     */
    protected void showGUIPage(String title, String file, AbstractController controller) {
        Stage stage = ((GUI) client.getUI()).stage();
        stage.setTitle(title);
        stage.setOnCloseRequest(t -> {
            if (client.getConnection() != null)
                client.getConnection().close();
            Platform.exit();
        });  // stop process when window is closed

        stage.setResizable(true);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));

        controller.setClient(client);
        controller.setStage(stage);

        loader.setController(controller);

        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}

package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.model.entity.Game;
import org.jline.reader.History;
import org.jline.terminal.Terminal;

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

    public abstract void draw (Client client);

}

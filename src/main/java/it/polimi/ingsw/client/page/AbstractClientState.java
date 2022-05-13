package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.model.entity.Game;
import org.jline.reader.History;
import org.jline.terminal.Terminal;

import java.util.logging.Logger;

public abstract class AbstractClientState {
    protected Client client;
    protected CLI cli;
    protected Terminal terminal;
    protected Game model;
    protected Logger LOGGER;
    protected History commandsHistory;

    public AbstractClientState (Client client) {
        this.client = client;
        model = client.getModel();
        LOGGER = client.getLogger();

        if (client.getUI().getClass().equals(CLI.class)) {
            cli = (CLI) client.getUI();
            terminal = cli.getTerminal();
            commandsHistory = cli.getCommandsHistory();
        }

    }

    public abstract void draw(Client client);

}

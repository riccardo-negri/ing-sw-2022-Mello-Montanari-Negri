package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.model.entity.Game;
import org.jline.terminal.Terminal;

import java.util.logging.Logger;

public abstract class AbstractClientState {
    protected Client client;
    protected final CLI cli;
    protected final Terminal terminal;
    protected final Game model;
    protected final Logger LOGGER;

    public AbstractClientState (Client client) {
        this.client = client;
        cli = (CLI) client.getUI();
        terminal = cli.getTerminal();
        model = client.getModel();
        LOGGER = client.getLogger();
    }

    public abstract void draw(Client client);

}

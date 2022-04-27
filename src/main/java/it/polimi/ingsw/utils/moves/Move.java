package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.server.Wizard;
import it.polimi.ingsw.utils.MessageContent;

public abstract class Move extends MessageContent {
    private Wizard wizard;

    public abstract void applyEffect(/*Model*/);
    public abstract void validate(/*Model*/) throws Exception;
}

package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.utils.MessageContent;

public abstract class Move extends MessageContent {
    private Wizard wizard;

    public abstract void applyEffect(Game game, Wizard wizard) throws Exception;
    public abstract void validate(Game game, Wizard wizard) throws Exception;
}

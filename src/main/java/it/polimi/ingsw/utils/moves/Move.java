package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.utils.MessageContent;

import java.util.ArrayList;
import java.util.List;

public abstract class Move extends MessageContent {
    // can be empty if no student is extracted for this move
    private List<StudentColor> extracted = new ArrayList<>();

    protected abstract void applyEffect(Game game, Wizard wizard) throws Exception;

    public void applyEffectServer(Game game, Wizard wizard) throws Exception {
        applyEffect(game, wizard);
        extracted = game.getBag().takeRecentlySelected();
    }

    public void applyEffectClient(Game game, Wizard wizard) throws Exception {
        game.getBag().putRecentlySelected(extracted);
        applyEffect(game, wizard);
    }

    public abstract void validate(Game game, Wizard wizard) throws Exception;
}

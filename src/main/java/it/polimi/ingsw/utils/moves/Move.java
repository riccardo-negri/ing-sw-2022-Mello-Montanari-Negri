package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.utils.MessageContent;

import java.util.ArrayList;

public abstract class Move extends MessageContent {
    // can be empty if no student is extracted for this move
    // must be ArrayList because it is serializable
    private final ArrayList<StudentColor> extracted = new ArrayList<>();
    protected int authorId;  // the wizard that required this move

    public Move(Wizard author) {
        this.authorId = author.getId();
    }

    protected abstract void applyEffect(Game game) throws Exception;

    public void applyEffectServer(Game game, Wizard wizard) throws Exception {
        this.authorId = wizard.getId();
        applyEffect(game);
        extracted.clear();
        extracted.addAll(game.getBag().takeRecentlySelected());
    }

    public void applyEffectClient(Game game) throws Exception {
        game.getBag().putRecentlySelected(extracted);
        applyEffect(game);
        //TODO: flush out queue
    }

    public abstract void validate(Game game) throws Exception;
}

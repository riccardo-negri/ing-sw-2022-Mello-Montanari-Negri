package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.Message;

import java.util.ArrayList;

public abstract class Move implements Message {
    // can be empty if no student is extracted for this move
    // must be ArrayList because it is serializable
    private final ArrayList<StudentColor> extracted = new ArrayList<>();
    protected int authorId;  // the wizard that required this move
    private int number = 0;

    protected Move(Wizard author) {
        this.authorId = author.getId();
    }

    protected abstract void applyEffect(Game game) throws GameRuleException;

    public void applyEffectServer(Game game, Wizard wizard) throws GameRuleException {
        this.authorId = wizard.getId();
        applyEffect(game);
        extracted.clear();
        extracted.addAll(game.getBag().takeRecentlySelected());
    }

    public void applyEffectClient(Game game) throws GameRuleException {
        game.getBag().putRecentlySelected(extracted);
        applyEffect(game);
        //TODO: flush out queue
    }

    public abstract void validate(Game game) throws GameRuleException;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

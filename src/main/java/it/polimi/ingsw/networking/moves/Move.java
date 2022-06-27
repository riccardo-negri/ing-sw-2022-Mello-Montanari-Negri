package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
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
    private int number = 0; // sending order number

    /**
     * create a message requesting to play a move
     * @param author the id of the wizard who required the move
     */
    protected Move(Wizard author) {
        this.authorId = author.getId();
    }

    /**
     * change the game state according to the move effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    protected abstract void applyEffect(Game game) throws GameRuleException;

    /**
     * apply the effect and save the extracted students in extracted attribute
     * @param game the game that is modified by the effect
     * @param wizard the id of the wizard who required the move
     * @throws GameRuleException if the move is not valid
     */
    public void applyEffectServer(Game game, Wizard wizard) throws GameRuleException {
        this.authorId = wizard.getId();
        applyEffect(game);
        extracted.clear();
        extracted.addAll(game.getBag().takeRecentlySelected());
    }

    /**
     * read the students that must be extracted from extracted attribute to have the same output as server
     * then apply move effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    public void applyEffectClient(Game game) throws GameRuleException {
        game.getBag().putRecentlySelected(extracted);
        applyEffect(game);
        game.getBag().takeRecentlySelected();  // flush out queue
    }

    /**
     * check the validity of the move without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    public abstract void validate(Game game) throws GameRuleException;

    /**
     * get number value
     * @return sending order number
     */
    public int getNumber() {
        return number;
    }

    /**
     * set number value on send
     * @param number sending order value
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * get authorId value
     * @return the id of the wizard who required the move
     */
    public int getAuthorId() {
        return authorId;
    }
}

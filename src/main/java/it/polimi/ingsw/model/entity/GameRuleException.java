package it.polimi.ingsw.model.entity;

/**
 * Exception subclass for game logic issues
 */
public class GameRuleException extends Exception{
    public GameRuleException(String m) {
        super(m);
    }
}

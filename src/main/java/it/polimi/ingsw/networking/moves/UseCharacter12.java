package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterTwelve;
import it.polimi.ingsw.model.enums.StudentColor;

/**
 * represents the move of activating the effect of character twelve
 */
public class UseCharacter12 extends UseCharacter {
    private final StudentColor color;

    /**
     * create a message requesting to activate character 12
     * @param author the id of the wizard who required the move
     * @param color color of students to return to the bag
     */
    public UseCharacter12(Wizard author, StudentColor color) {
        super(author);
        this.color = color;
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterTwelve character = ((CharacterTwelve) game.getCharacter(12));
        character.useEffect(authorId, color);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterTwelve character = ((CharacterTwelve) game.getCharacter(12));
        character.characterTwelveValidator(authorId);
    }

    /**
     * get color value
     * @return color of students to return to the bag
     */
    public StudentColor getColor() {
        return color;
    }
}

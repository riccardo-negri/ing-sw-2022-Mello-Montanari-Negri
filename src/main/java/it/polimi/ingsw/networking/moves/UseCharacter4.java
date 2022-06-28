package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFour;

/**
 * represents the move of activating the effect of character four
 */
public class UseCharacter4 extends UseCharacter {

    /**
     * create a message requesting to activate character 4
     * @param author the id of the wizard who required the move
     */
    public UseCharacter4(Wizard author) {
        super(author);
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterFour character = ((CharacterFour) game.getCharacter(4));
        character.useEffect(authorId);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterFour character = ((CharacterFour) game.getCharacter(4));
        character.characterFourValidator(authorId);
    }
}

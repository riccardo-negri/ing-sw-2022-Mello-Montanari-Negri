package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterEight;

/**
 * represents the move of activating the effect of character eight
 */
public class UseCharacter8 extends UseCharacter {
    /**
     * create a message requesting to activate character 8
     * @param author the id of the wizard who required the move
     */
    public UseCharacter8(Wizard author) {
        super(author);
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterEight character = ((CharacterEight) game.getCharacter(8));
        character.useEffect(authorId);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterEight character = ((CharacterEight) game.getCharacter(8));
        character.characterEightValidator(authorId);
    }
}

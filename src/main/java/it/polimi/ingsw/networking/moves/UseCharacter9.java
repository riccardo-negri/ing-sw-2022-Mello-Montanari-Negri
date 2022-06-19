package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterNine;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter9 extends UseCharacter {
    private final StudentColor color;

    /**
     * create a message requesting to activate character 9
     * @param author the id of the wizard who required the move
     * @param color the color that in this turn will add no influence
     */
    public UseCharacter9(Wizard author, StudentColor color) {
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
        CharacterNine character = ((CharacterNine) game.getCharacter(9));
        character.useEffect(authorId, color);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterNine character = ((CharacterNine) game.getCharacter(9));
        character.characterNineValidator(authorId);
    }

    /**
     * get color value
     * @return the color that in this turn will add no influence
     */
    public StudentColor getColor() {
        return color;
    }
}

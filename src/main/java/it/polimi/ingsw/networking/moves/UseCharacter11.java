package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterEleven;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter11 extends UseCharacter {
    private final StudentColor student;

    /**
     * create a message requesting to activate character 11
     * @param author the id of the wizard who required the move
     * @param student student to take from the character card and place in the dining room
     */
    public UseCharacter11(Wizard author, StudentColor student) {
        super(author);
        this.student = student;
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterEleven character = ((CharacterEleven) game.getCharacter(11));
        character.useEffect(authorId, student);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterEleven character = ((CharacterEleven) game.getCharacter(11));
        character.characterElevenValidator(authorId, student);
    }

    /**
     * get student value
     * @return student to take from the character card and place in the dining room
     */
    public StudentColor getStudent() {
        return student;
    }
}

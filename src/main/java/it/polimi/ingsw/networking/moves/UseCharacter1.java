package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterOne;
import it.polimi.ingsw.model.enums.StudentColor;

/**
 * represents the move of activating the effect of character one
 */
public class UseCharacter1 extends UseCharacter {
    private final StudentColor student;
    private final int islandId;

    /**
     * create a message requesting to activate character 1
     * @param author author the id of the wizard who required the move
     * @param student color of the student to take from the character card
     * @param islandId id of the island where to place the student
     */
    public UseCharacter1(Wizard author, StudentColor student, int islandId) {
        super(author);
        this.student = student;
        this.islandId = islandId;
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterOne character = ((CharacterOne) game.getCharacter(1));
        character.useEffect(authorId, student, islandId);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterOne character = ((CharacterOne) game.getCharacter(1));
        character.characterOneValidator(authorId, student, islandId);
    }

    /**
     * get student value
     * @return color of the student to take from the character card
     */
    public StudentColor getStudent() {
        return student;
    }

    /**
     * get islandId value
     * @return id of the island where to place the student
     */
    public int getIslandId() {
        return islandId;
    }
}

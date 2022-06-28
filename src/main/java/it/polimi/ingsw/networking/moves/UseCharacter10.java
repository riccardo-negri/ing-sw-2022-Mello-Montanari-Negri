package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterTen;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

/**
 * represents the move of activating the effect of character ten
 */
public class UseCharacter10 extends UseCharacter {
    private final ArrayList<StudentColor> taken;
    private final ArrayList<StudentColor> given;

    /**
     * create a message requesting to activate character 10
     * @param author the id of the wizard who required the move
     * @param taken the list of students taken from the entrance
     * @param given the list of students given to the entrance
     */
    public UseCharacter10(Wizard author, List<StudentColor> taken, List<StudentColor> given) {
        super(author);
        this.taken = new ArrayList<>(taken);
        this.given = new ArrayList<>(given);
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterTen character = ((CharacterTen) game.getCharacter(10));
        character.useEffect(authorId, taken, given);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterTen character = ((CharacterTen) game.getCharacter(10));
        character.characterTenValidator(authorId, taken, given);
    }

    /**
     * get taken value
     * @return the list of students taken from the entrance
     */
    public List<StudentColor> getTaken() {
        return taken;
    }

    /**
     * get given value
     * @return the list of students given to the entrance
     */
    public List<StudentColor> getGiven() {
        return given;
    }
}

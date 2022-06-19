package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterThree;

public class UseCharacter3 extends UseCharacter {
    private final int islandGroupId;

    /**
     * create a message requesting to activate character 3
     * @param author the id of the wizard who required the move
     * @param islandGroupId the id of the island group to resolve as if mother nature was there
     */
    public UseCharacter3(Wizard author, int islandGroupId) {
        super(author);
        this.islandGroupId = islandGroupId;
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterThree character = ((CharacterThree) game.getCharacter(3));
        character.useEffect(authorId, islandGroupId);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterThree character = ((CharacterThree) game.getCharacter(3));
        character.characterThreeValidator(authorId, islandGroupId);
    }

    /**
     * get islandGroupId value
     * @return the id of the island group to resolve as if mother nature was there
     */
    public int getIslandGroupId() {
        return islandGroupId;
    }
}

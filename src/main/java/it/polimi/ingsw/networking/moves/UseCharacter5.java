package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFive;

public class UseCharacter5 extends UseCharacter {
    private final int islandId;

    /**
     * create a message requesting to activate character 5
     * @param author the id of the wizard who required the move
     * @param islandId the id of the island where to put the no entry tile
     */
    public UseCharacter5(Wizard author, int islandId) {
        super(author);
        this.islandId = islandId;
    }

    /**
     * change the game state according to the character effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterFive character = ((CharacterFive) game.getCharacter(5));
        character.useEffect(authorId, islandId);
    }

    /**
     * check the validity of the character activation without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterFive character = ((CharacterFive) game.getCharacter(5));
        character.characterFiveValidator(authorId, islandId);
    }

    /**
     * get islandId value
     * @return the id of the island where to put the no entry tile
     */
    public int getIslandId() {
        return islandId;
    }
}

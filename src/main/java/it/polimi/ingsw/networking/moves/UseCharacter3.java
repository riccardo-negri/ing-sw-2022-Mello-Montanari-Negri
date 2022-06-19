package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterThree;

public class UseCharacter3 extends UseCharacter {
    private final int islandGroupId;

    public UseCharacter3(Wizard author, int islandGroupId) {
        super(author);
        this.islandGroupId = islandGroupId;
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterThree character = ((CharacterThree) game.getCharacter(3));
        character.useEffect(authorId, islandGroupId);
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterThree character = ((CharacterThree) game.getCharacter(3));
        character.characterThreeValidator(authorId, islandGroupId);
    }

    public int getIslandGroupId() {
        return islandGroupId;
    }
}

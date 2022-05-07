package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterThree;

public class UseCharacter3 extends UseCharacter {
    private final int islandGroupId;

    public UseCharacter3(int islandGroupId) {
        this.islandGroupId = islandGroupId;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterThree character = ((CharacterThree) game.getCharacter(3));
        character.useEffect(wizard.getTowerColor(), islandGroupId);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterThree character = ((CharacterThree) game.getCharacter(3));
        character.characterThreeValidator(wizard.getTowerColor(), islandGroupId);
    }
}

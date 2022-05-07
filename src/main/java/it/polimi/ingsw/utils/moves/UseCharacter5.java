package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFive;

public class UseCharacter5 extends UseCharacter {
    private final int islandId;

    public UseCharacter5(int islandId) {
        this.islandId = islandId;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterFive character = ((CharacterFive) game.getCharacter(5));
        character.useEffect(wizard.getTowerColor(), islandId);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterFive character = ((CharacterFive) game.getCharacter(5));
        character.characterFiveValidator(wizard.getTowerColor(), islandId);
    }
}

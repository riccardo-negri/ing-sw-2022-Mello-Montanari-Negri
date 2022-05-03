package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterTwo;

public class UseCharacter2 extends UseCharacter {

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterTwo character = ((CharacterTwo) game.getCharacter(2));
        character.useEffect(wizard.getTowerColor());
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterTwo character = ((CharacterTwo) game.getCharacter(2));
        character.characterTwoValidator(wizard.getTowerColor());
    }
}

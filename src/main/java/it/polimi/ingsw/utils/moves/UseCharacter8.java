package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterEight;

public class UseCharacter8 extends UseCharacter {
    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterEight character = ((CharacterEight) game.getCharacter(8));
        character.useEffect(wizard.getTowerColor());
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterEight character = ((CharacterEight) game.getCharacter(8));
        character.characterEightValidator(wizard.getTowerColor());
    }
}

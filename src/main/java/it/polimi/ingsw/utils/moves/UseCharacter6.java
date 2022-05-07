package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterSix;

public class UseCharacter6 extends UseCharacter {

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterSix character = ((CharacterSix) game.getCharacter(6));
        character.useEffect(wizard.getTowerColor());
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterSix character = ((CharacterSix) game.getCharacter(6));
        character.characterSixValidator(wizard.getTowerColor());
    }
}

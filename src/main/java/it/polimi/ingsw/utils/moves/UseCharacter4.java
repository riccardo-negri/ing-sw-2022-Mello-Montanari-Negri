package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFour;

public class UseCharacter4 extends UseCharacter {

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterFour character = ((CharacterFour) game.getCharacter(4));
        character.useEffect(wizard.getTowerColor());
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterFour character = ((CharacterFour) game.getCharacter(4));
        character.characterFourValidator(wizard.getTowerColor());
    }
}

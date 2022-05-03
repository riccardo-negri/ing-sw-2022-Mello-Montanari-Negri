package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterNine;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter9 extends UseCharacter {
    private final StudentColor color;

    public UseCharacter9(StudentColor color) {
        this.color = color;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterNine character = ((CharacterNine) game.getCharacter(9));
        character.useEffect(wizard.getTowerColor(), color);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterNine character = ((CharacterNine) game.getCharacter(9));
        character.characterNineValidator(wizard.getTowerColor());
    }
}

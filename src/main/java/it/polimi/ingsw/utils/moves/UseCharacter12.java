package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterTwelve;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter12 extends UseCharacter {
    private final StudentColor color;

    public UseCharacter12(StudentColor color) {
        this.color = color;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterTwelve character = ((CharacterTwelve) game.getCharacter(12));
        character.useEffect(wizard.getTowerColor(), color);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterTwelve character = ((CharacterTwelve) game.getCharacter(12));
        character.characterTwelveValidator(wizard.getTowerColor());
    }
}

package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterEleven;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter11 extends UseCharacter {
    private final StudentColor student;

    public UseCharacter11(StudentColor student) {
        this.student = student;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterEleven character = ((CharacterEleven) game.getCharacter(11));
        character.useEffect(wizard.getTowerColor(), student);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterEleven character = ((CharacterEleven) game.getCharacter(11));
        character.characterElevenValidator(wizard.getTowerColor(), student);
    }
}

package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterSeven;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

public class UseCharacter7 extends UseCharacter {
    private final ArrayList<StudentColor> taken;
    private final ArrayList<StudentColor> given;

    public UseCharacter7(List<StudentColor> taken, List<StudentColor> given) {
        this.taken = new ArrayList<>(taken);
        this.given = new ArrayList<>(given);
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterSeven character = ((CharacterSeven) game.getCharacter(7));
        character.useEffect(wizard.getTowerColor(), taken, given);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterSeven character = ((CharacterSeven) game.getCharacter(7));
        character.characterSevenValidator(wizard.getTowerColor(), taken, given);
    }
}

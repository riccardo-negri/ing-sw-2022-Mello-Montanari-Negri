package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterOne;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter1 extends UseCharacter {
    private final StudentColor student;
    private final int islandId;

    public UseCharacter1(StudentColor student, int islandId) {
        this.student = student;
        this.islandId = islandId;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        CharacterOne character = ((CharacterOne) game.getCharacter(1));
        character.useEffect(wizard.getTowerColor(), student, islandId);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        CharacterOne character = ((CharacterOne) game.getCharacter(1));
        character.characterOneValidator(wizard.getTowerColor(), student, islandId);
    }
}

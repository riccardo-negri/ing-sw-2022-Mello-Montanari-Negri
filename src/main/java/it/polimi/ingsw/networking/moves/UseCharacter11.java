package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterEleven;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter11 extends UseCharacter {
    private final StudentColor student;

    public UseCharacter11(Wizard author, StudentColor student) {
        super(author);
        this.student = student;
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterEleven character = ((CharacterEleven) game.getCharacter(11));
        character.useEffect(authorId, student);
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterEleven character = ((CharacterEleven) game.getCharacter(11));
        character.characterElevenValidator(authorId, student);
    }

    public StudentColor getStudent() {
        return student;
    }
}

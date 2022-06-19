package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterOne;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter1 extends UseCharacter {
    private final StudentColor student;
    private final int islandId;

    public UseCharacter1(Wizard author, StudentColor student, int islandId) {
        super(author);
        this.student = student;
        this.islandId = islandId;
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterOne character = ((CharacterOne) game.getCharacter(1));
        character.useEffect(authorId, student, islandId);
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterOne character = ((CharacterOne) game.getCharacter(1));
        character.characterOneValidator(authorId, student, islandId);
    }

    public StudentColor getStudent() {
        return student;
    }

    public int getIslandId() {
        return islandId;
    }
}

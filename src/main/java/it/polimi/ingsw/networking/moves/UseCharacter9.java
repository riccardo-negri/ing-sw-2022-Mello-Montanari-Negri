package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterNine;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter9 extends UseCharacter {
    private final StudentColor color;

    public UseCharacter9(Wizard author, StudentColor color) {
        super(author);
        this.color = color;
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterNine character = ((CharacterNine) game.getCharacter(9));
        character.useEffect(authorId, color);
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterNine character = ((CharacterNine) game.getCharacter(9));
        character.characterNineValidator(authorId);
    }
}

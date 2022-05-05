package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterTwo;

public class UseCharacter2 extends UseCharacter {

    public UseCharacter2(Wizard author) {
        super(author);
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        CharacterTwo character = ((CharacterTwo) game.getCharacter(2));
        character.useEffect(authorId);
    }

    @Override
    public void validate(Game game) throws Exception {
        CharacterTwo character = ((CharacterTwo) game.getCharacter(2));
        character.characterTwoValidator(authorId);
    }
}

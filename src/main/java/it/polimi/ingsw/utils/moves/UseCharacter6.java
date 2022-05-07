package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterSix;

public class UseCharacter6 extends UseCharacter {

    public UseCharacter6(Wizard author) {
        super(author);
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        CharacterSix character = ((CharacterSix) game.getCharacter(6));
        character.useEffect(authorId);
    }

    @Override
    public void validate(Game game) throws Exception {
        CharacterSix character = ((CharacterSix) game.getCharacter(6));
        character.characterSixValidator(authorId);
    }
}

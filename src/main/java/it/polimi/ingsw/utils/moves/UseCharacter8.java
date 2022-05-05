package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterEight;

public class UseCharacter8 extends UseCharacter {
    public UseCharacter8(Wizard author) {
        super(author);
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        CharacterEight character = ((CharacterEight) game.getCharacter(8));
        character.useEffect(authorId);
    }

    @Override
    public void validate(Game game) throws Exception {
        CharacterEight character = ((CharacterEight) game.getCharacter(8));
        character.characterEightValidator(authorId);
    }
}

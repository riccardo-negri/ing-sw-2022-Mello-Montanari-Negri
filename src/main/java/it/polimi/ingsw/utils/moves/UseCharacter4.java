package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFour;

public class UseCharacter4 extends UseCharacter {

    public UseCharacter4(Wizard author) {
        super(author);
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        CharacterFour character = ((CharacterFour) game.getCharacter(4));
        character.useEffect(authorId);
    }

    @Override
    public void validate(Game game) throws Exception {
        CharacterFour character = ((CharacterFour) game.getCharacter(4));
        character.characterFourValidator(authorId);
    }
}

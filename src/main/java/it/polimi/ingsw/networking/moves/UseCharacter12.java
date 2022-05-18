package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterTwelve;
import it.polimi.ingsw.model.enums.StudentColor;

public class UseCharacter12 extends UseCharacter {
    private final StudentColor color;

    public UseCharacter12(Wizard author, StudentColor color) {
        super(author);
        this.color = color;
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        CharacterTwelve character = ((CharacterTwelve) game.getCharacter(12));
        character.useEffect(authorId, color);
    }

    @Override
    public void validate(Game game) throws Exception {
        CharacterTwelve character = ((CharacterTwelve) game.getCharacter(12));
        character.characterTwelveValidator(authorId);
    }
}

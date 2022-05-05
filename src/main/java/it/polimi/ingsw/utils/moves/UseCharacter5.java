package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFive;

public class UseCharacter5 extends UseCharacter {
    private final int islandId;

    public UseCharacter5(Wizard author, int islandId) {
        super(author);
        this.islandId = islandId;
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        CharacterFive character = ((CharacterFive) game.getCharacter(5));
        character.useEffect(authorId, islandId);
    }

    @Override
    public void validate(Game game) throws Exception {
        CharacterFive character = ((CharacterFive) game.getCharacter(5));
        character.characterFiveValidator(authorId, islandId);
    }
}

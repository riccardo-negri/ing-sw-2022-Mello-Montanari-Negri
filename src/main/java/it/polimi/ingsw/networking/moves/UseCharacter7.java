package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterSeven;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

public class UseCharacter7 extends UseCharacter {
    private final ArrayList<StudentColor> taken;
    private final ArrayList<StudentColor> given;

    public UseCharacter7(Wizard author, List<StudentColor> taken, List<StudentColor> given) {
        super(author);
        this.taken = new ArrayList<>(taken);
        this.given = new ArrayList<>(given);
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        CharacterSeven character = ((CharacterSeven) game.getCharacter(7));
        character.useEffect(authorId, taken, given);
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        CharacterSeven character = ((CharacterSeven) game.getCharacter(7));
        character.characterSevenValidator(authorId, taken, given);
    }

    public ArrayList<StudentColor> getGiven() {
        return given;
    }

    public ArrayList<StudentColor> getTaken() {
        return taken;
    }
}

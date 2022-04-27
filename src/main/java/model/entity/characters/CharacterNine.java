package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;
import model.enums.StudentColor;

public class CharacterNine extends Character{

    private StudentColor studentColor;

    public CharacterNine () {
        super(3);
    }

    public void useEffect(Wizard player, Game game, StudentColor studentColor) throws Exception{
        useCard(player);
        this.studentColor = studentColor;
        ActionState state = (ActionState) game.getGameState();
        state.activateEffect(this);
    }

    public StudentColor getStudentColor() { return studentColor; }
}

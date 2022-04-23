package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public class CharacterSix extends Character{

    public CharacterSix() { super(3); }

    public void useEffect(Wizard player, Game game) throws Exception{
        useCard(player);
        ActionState state = (ActionState) game.getGameState();
        state.activateEffect(this);
    }
}

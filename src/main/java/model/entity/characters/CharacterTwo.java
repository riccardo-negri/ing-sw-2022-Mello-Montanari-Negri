package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public class CharacterTwo extends Character{

    public CharacterTwo() {
        super(2);
    }

    @Override
    public void useEffect(Wizard player, Game game, Object request) throws Exception {
        useCard(player);
        ActionState state = (ActionState) game.getGameState();
        state.activateEffectTwo(player);
    }
}

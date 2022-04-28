package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public class CharacterEight extends Character {

    public CharacterEight () {
        super(2);
    }

    /**
     * During the influence calculation, two points are added to the activator
     * @param player the player playing the card
     * @param game the current game
     */
    public void useEffect(Wizard player, Game game) throws Exception{
        characterEightValidator(player, game);
        useCard(player);
        ActionState state = (ActionState) game.getGameState();
        state.activateEffect(this);
    }

    /**
     * During the influence calculation, two points are added to the activator
     * @param player the player playing the card
     * @param game the current game
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterEightValidator(Wizard player, Game game) throws Exception {
        characterValidator(player, game);
    }
}

package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public class CharacterSix extends Character{

    public CharacterSix() { super(3); }

    /**
     * towers are not counted when calculating the influence of an island group
     * @param player the player playing the card
     * @param game the current game
     */
    public void useEffect(Wizard player, Game game) throws Exception{
        characterSixValidator(player, game);
        useCard(player);
        ((ActionState) game.getGameState()).activateEffect(this);
    }

    /**
     * Validator for character six useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterSixValidator(Wizard player, Game game) throws Exception {
        characterValidator(player, game);
    }
}

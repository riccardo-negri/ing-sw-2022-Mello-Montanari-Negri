package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public class CharacterFour extends Character{

    public CharacterFour() {
        super(1);
    }

    /**
     * mother nature can do two more steps than the assistant card says
     * @param player the player playing the card
     * @param game the current game
     */
    public void useEffect(Wizard player, Game game) throws Exception {
        characterFourValidator(player, game);
        useCard(player);
        ((ActionState) game.getGameState()).activateEffect(this);
    }

    /**
     * Validator for character four useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterFourValidator(Wizard player, Game game) throws Exception {
        characterValidator(player, game);
    }
}

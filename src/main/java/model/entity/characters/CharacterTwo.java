package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public class CharacterTwo extends Character{

    public CharacterTwo() {
        super(2);
    }

    /**
     * for the turn take control of professors even if the number of student is the same as the one controlling them
     * @param player the player playing the card
     * @param game the current game
     */
    public void useEffect(Wizard player, Game game) throws Exception {
        characterTwoValidator(player, game);
        useCard(player);
        ((ActionState) game.getGameState()).activateEffect(this);
        activator = player;
    }

    /**
     * Validator for character two useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterTwoValidator(Wizard player, Game game) throws Exception {
        characterValidator(player, game);
    }
}

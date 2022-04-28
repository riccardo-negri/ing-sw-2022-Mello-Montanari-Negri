package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;

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

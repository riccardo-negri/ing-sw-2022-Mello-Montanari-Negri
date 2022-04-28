package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;

public class CharacterSix extends Character{

    public CharacterSix(Integer characterId) { super(characterId, 3); }

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

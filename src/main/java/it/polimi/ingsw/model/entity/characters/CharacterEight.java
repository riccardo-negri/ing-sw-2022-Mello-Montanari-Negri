package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;

public class CharacterEight extends Character {

    public CharacterEight (Integer characterId) {
        super(characterId, 2);
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

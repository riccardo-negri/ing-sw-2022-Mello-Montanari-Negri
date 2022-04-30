package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.Tower;

public class CharacterTwo extends Character{

    public CharacterTwo(Integer gameId, Integer characterId) {
        super(gameId, characterId, 2);
    }

    /**
     * for the turn take control of professors even if the number of student is the same as the one controlling them
     * @param playingTower the player playing the card
     */
    public void useEffect(Tower playingTower) throws Exception {
        characterTwoValidator(playingTower);
        useCard(playingTower);
        ((ActionState) Game.request(gameId).getGameState()).activateEffect(this);
    }

    /**
     * Validator for character two useEffect method
     * @param playingTower the player playing the card
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterTwoValidator(Tower playingTower) throws Exception {
        characterValidator(playingTower);
    }
}

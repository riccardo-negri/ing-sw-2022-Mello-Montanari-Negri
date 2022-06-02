package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.Tower;

public class CharacterEight extends Character {

    public CharacterEight (Integer gameId, Integer characterId) {
        super(gameId, characterId, 2);
    }

    /**
     * During the influence calculation, two points are added to the activator
     * @param playingWizard the player playing the card
     */
    public void useEffect(Integer playingWizard) throws GameRuleException {
        characterEightValidator(playingWizard);
        useCard(playingWizard);
    }

    /**
     * During the influence calculation, two points are added to the activator
     * @param playingWizard the player playing the card
     * @throws GameRuleException if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterEightValidator(Integer playingWizard) throws GameRuleException {
        characterValidator(playingWizard);
    }
}

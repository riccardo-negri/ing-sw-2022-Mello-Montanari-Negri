package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;

public class CharacterFour extends Character{

    public CharacterFour(Integer gameId, Integer characterId) {
        super(gameId, characterId, 1);
    }

    /**
     * mother nature can do two more steps than the assistant card says
     * @param playingWizard the player playing the card
     */
    public void useEffect(Integer playingWizard) throws GameRuleException {
        characterFourValidator(playingWizard);
        useCard(playingWizard);
    }

    /**
     * Validator for character four useEffect method
     * @param playingWizard the player playing the card
     * @throws GameRuleException if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterFourValidator(Integer playingWizard) throws GameRuleException {
        characterValidator(playingWizard);
    }
}

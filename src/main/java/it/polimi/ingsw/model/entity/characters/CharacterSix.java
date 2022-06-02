package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;

public class CharacterSix extends Character{

    public CharacterSix(Integer gameId, Integer characterId) { super(gameId, characterId, 3); }

    /**
     * towers are not counted when calculating the influence of an island group
     * @param playingWizard the player playing the card
     */
    public void useEffect(Integer playingWizard) throws GameRuleException{
        characterSixValidator(playingWizard);
        useCard(playingWizard);
    }

    /**
     * Validator for character six useEffect method
     * @param playingWizard the player playing the card
     * @throws GameRuleException if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterSixValidator(Integer playingWizard) throws GameRuleException {
        characterValidator(playingWizard);
    }
}

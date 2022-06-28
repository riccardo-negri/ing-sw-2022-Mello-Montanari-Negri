package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;

/**
 * character 3 class with logic
 */
public class CharacterThree extends Character {

    public CharacterThree(Integer gameId, Integer characterId) {
        super(gameId, characterId, 3);
    }

    /**
     * update the owner of an island group without the need of mother nature
     * @param playingWizard the player playing the card
     * @param islandGroupId the group to update the owner on
     */
    public void useEffect(Integer playingWizard, Integer islandGroupId) throws GameRuleException {
        characterThreeValidator(playingWizard, islandGroupId);
        useCard(playingWizard);
        Game.request(gameId).getIslandGroup(islandGroupId).updateTower(Game.request(gameId), null);
        Game.request(gameId).unifyIslands();
    }

    /**
     * validator for character three useEffect method
     * @param playingWizard the player playing the card
     * @param islandGroupId the group to update the owner on
     * @throws GameRuleException if it is not the player turn, he does not have enough money to activate the card,
     * or the island group does not exist
     */
    public void characterThreeValidator(Integer playingWizard, Integer islandGroupId) throws GameRuleException {
        characterValidator(playingWizard);
        if (Game.request(gameId).getIslandGroup(islandGroupId) == null) throw new GameRuleException("Selected island group doesn't exist");
    }
}

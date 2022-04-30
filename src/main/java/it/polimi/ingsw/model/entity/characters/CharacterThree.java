package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.IslandGroup;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.Tower;

public class CharacterThree extends Character {

    public CharacterThree(Integer gameId, Integer characterId) {
        super(gameId, characterId, 3);
    }

    /**
     * update the owner of an island group without the need of mother nature
     * @param playingTower the player playing the card
     * @param islandGroupId the group to update the owner on
     */
    public void useEffect(Tower playingTower, Integer islandGroupId) throws Exception {
        characterThreeValidator(playingTower, islandGroupId);
        useCard(playingTower);
        Game.request(gameId).getIslandGroup(islandGroupId).updateTower(Game.request(gameId), null);
        Game.request(gameId).unifyIslands();
    }

    /**
     * validator for character three useEffect method
     * @param playingTower the player playing the card
     * @param islandGroupId the group to update the owner on
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * or the island group does not exist
     */
    public void characterThreeValidator(Tower playingTower, Integer islandGroupId) throws Exception {
        characterValidator(playingTower);
        Game.request(gameId).getIslandGroup(islandGroupId);
    }
}

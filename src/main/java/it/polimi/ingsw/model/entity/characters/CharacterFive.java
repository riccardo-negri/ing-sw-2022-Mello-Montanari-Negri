package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;

/**
 * character 5 class with logic
 */
public class CharacterFive extends Character{

    private Integer stopNumber;

    public CharacterFive(Integer gameId, Integer characterId) {
        super(gameId, characterId, 2);
        stopNumber = 4;
    }

    /**
     * a stop card can be put on any island to prevent a change of owner
     * @param playingWizard the player playing the card
     * @param islandId the island to put the student on
     */
    public void useEffect(Integer playingWizard, Integer islandId) throws GameRuleException{
        characterFiveValidator(playingWizard, islandId);
        useCard(playingWizard);
        Game.request(gameId).getIsland(islandId).addStopCard(this);
    }

    /**
     * Validator for character five useEffect method
     * @param playingWizard the player playing the card
     * @param islandId the island to put the student on
     * @throws GameRuleException if it is not the player turn, he does not have enough money to activate the card,
     * there are no more stop card available, or the island does not exist
     */
    public void characterFiveValidator(Integer playingWizard, Integer islandId) throws GameRuleException {
        characterValidator(playingWizard);
        if(stopNumber <= 0) throw new GameRuleException("No more stop cards available");
        if (islandId<0 || islandId>=12) throw new GameRuleException("Island does not exist");
        if (Game.request(gameId).getIsland(islandId).hasStopCard()) throw new GameRuleException("A stop card is already on the island");
    }

    public void removeOneCard() { stopNumber -= 1; }

    public void addOneCard() { stopNumber += 1; }

    public Integer getStopNumber () {
        return stopNumber;
    }
}

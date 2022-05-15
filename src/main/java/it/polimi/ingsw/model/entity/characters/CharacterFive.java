package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Island;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.Tower;

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
    public void useEffect(Integer playingWizard, Integer islandId) throws Exception{
        characterFiveValidator(playingWizard, islandId);
        useCard(playingWizard);
        Game.request(gameId).getIsland(islandId).addStopCard(this);
    }

    /**
     * Validator for character five useEffect method
     * @param playingWizard the player playing the card
     * @param islandId the island to put the student on
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * there are no more stop card available, or the island does not exist
     */
    public void characterFiveValidator(Integer playingWizard, Integer islandId) throws Exception {
        characterValidator(playingWizard);
        if(stopNumber <= 0) throw new Exception("No more stop cards available");
        if (islandId<0 || islandId>=12) throw new Exception("Island does not exist");
    }

    public void removeOneCard() { stopNumber -= 1; }

    public void addOneCard() { stopNumber += 1; }

    public Integer getStopNumber () {
        return stopNumber;
    }
}

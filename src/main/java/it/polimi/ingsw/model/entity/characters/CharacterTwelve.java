package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.StudentColor;

/**
 * character 12 class with logic
 */
public class CharacterTwelve extends Character{

    public CharacterTwelve (Integer gameId, Integer characterId) {
        super(gameId, characterId, 3);
    }

    /**
     * All the player have to remove three students of the color (if present) from the dining room
     * @param playingWizard the player playing the card
     * @param studentColor the color of the student to remove from the dining room
     */
    public void useEffect(Integer playingWizard, StudentColor studentColor) throws GameRuleException {
        characterTwelveValidator(playingWizard);
        useCard(playingWizard);
        for (int i=0; i<Game.request(gameId).getPlayerNumber().getWizardNumber(); i++)
            for(int j=0; j<3; j++)
                Game.request(gameId).getWizard(i).takeDiningStudent(studentColor);
    }

    /**
     * All the player have to remove three students of the color (if present) from the dining room
     * @param playingWizard the player playing the card
     * @throws GameRuleException if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterTwelveValidator(Integer playingWizard) throws GameRuleException {
        characterValidator(playingWizard);
    }
}

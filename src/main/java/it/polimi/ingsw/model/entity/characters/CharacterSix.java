package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.Tower;

public class CharacterSix extends Character{

    public CharacterSix(Integer gameId, Integer characterId) { super(gameId, characterId, 3); }

    /**
     * towers are not counted when calculating the influence of an island group
     * @param playingWizard the player playing the card
     */
    public void useEffect(Integer playingWizard) throws Exception{
        characterSixValidator(playingWizard);
        useCard(playingWizard);
    }

    /**
     * Validator for character six useEffect method
     * @param playingWizard the player playing the card
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterSixValidator(Integer playingWizard) throws Exception {
        characterValidator(playingWizard);
    }
}

package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.Tower;

public class CharacterFour extends Character{

    public CharacterFour(Integer gameId, Integer characterId) {
        super(gameId, characterId, 1);
    }

    /**
     * mother nature can do two more steps than the assistant card says
     * @param playingWizard the player playing the card
     */
    public void useEffect(Integer playingWizard) throws Exception {
        characterFourValidator(playingWizard);
        useCard(playingWizard);
        ((ActionState) Game.request(gameId).getGameState()).activateEffect(this);
    }

    /**
     * Validator for character four useEffect method
     * @param playingWizard the player playing the card
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterFourValidator(Integer playingWizard) throws Exception {
        characterValidator(playingWizard);
    }
}

package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

public class CharacterNine extends Character{

    private StudentColor studentColor;

    public CharacterNine (Integer gameId, Integer characterId) {
        super(gameId, characterId, 3);
    }

    /**
     * during the influence calculation on student color is not considered
     * @param playingWizard the player playing the card
     * @param studentColor the color not to consider in the calculation
     */
    public void useEffect(Integer playingWizard, StudentColor studentColor) throws Exception {
        characterNineValidator(playingWizard);
        useCard(playingWizard);
        this.studentColor = studentColor;
        ((ActionState) Game.request(gameId).getGameState()).activateEffect(this);
    }

    /**
     * Validator for character nine useEffect method
     * @param playingWizard the player playing the card
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterNineValidator(Integer playingWizard) throws Exception {
        characterValidator(playingWizard);
    }

    public StudentColor getStudentColor() { return studentColor; }
}

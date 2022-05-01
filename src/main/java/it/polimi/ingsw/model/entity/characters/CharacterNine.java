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
     * @param playingTower the player playing the card
     * @param studentColor the color not to consider in the calculation
     */
    public void useEffect(Tower playingTower, StudentColor studentColor) throws Exception{
        characterNineValidator(playingTower);
        useCard(playingTower);
        this.studentColor = studentColor;
        ((ActionState) Game.request(gameId).getGameState()).activateEffect(this);
    }

    /**
     * Validator for character nine useEffect method
     * @param playingTower the player playing the card
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterNineValidator(Tower playingTower) throws Exception {
        characterValidator(playingTower);
    }

    public StudentColor getStudentColor() { return studentColor; }
}

package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.entity.gameState.ActionState;
import model.enums.StudentColor;

public class CharacterNine extends Character{

    private StudentColor studentColor;

    public CharacterNine () {
        super(3);
    }

    /**
     * during the influence calculation on student color is not considered
     * @param player the player playing the card
     * @param game the current game
     * @param studentColor the color not to consider in the calculation
     */
    public void useEffect(Wizard player, Game game, StudentColor studentColor) throws Exception{
        characterNineValidator(player, game);
        useCard(player);
        this.studentColor = studentColor;
        ((ActionState) game.getGameState()).activateEffect(this);
    }

    /**
     * Validator for character nine useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterNineValidator(Wizard player, Game game) throws Exception {
        characterValidator(player, game);
    }

    public StudentColor getStudentColor() { return studentColor; }
}

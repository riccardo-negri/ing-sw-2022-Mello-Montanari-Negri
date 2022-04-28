package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.enums.StudentColor;
import model.enums.Tower;

public class CharacterTwelve extends Character{

    public CharacterTwelve () {
        super(3);
    }

    /**
     * All the player have to remove three students of the color (if present) from the dining room
     * @param player the player playing the card
     * @param game the current game
     * @param studentColor the color of the student to remove from the dining room
     */
    public void useEffect(Wizard player, Game game, StudentColor studentColor) throws Exception {
        useCard(player);
        for (int i=0; i<game.getPlayerNumber().getTowerNumber(); i++)
            for(int j=0; j<3; j++)
                game.getWizard(Tower.fromNumber(i)).takeDiningStudent(studentColor);
    }

    /**
     * All the player have to remove three students of the color (if present) from the dining room
     * @param player the player playing the card
     * @param game the current game
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    public void characterTwelveValidator(Wizard player, Game game) throws Exception {
        characterValidator(player, game);
    }
}

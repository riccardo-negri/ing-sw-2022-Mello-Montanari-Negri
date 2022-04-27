package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.enums.PlayerNumber;
import model.enums.StudentColor;
import model.enums.Tower;

public class CharacterTwelve extends Character{

    public CharacterTwelve () {
        super(3);
    }

    public void useEffect(Wizard player, Game game, StudentColor studentColor) throws Exception{
        useCard(player);
        for (int i=0; i<game.getPlayerNumber().getTowerNumber(); i++) {
            for(int j=0; j<3; j++) {
                game.getWizard(Tower.fromNumber(i)).takeDiningStudent(studentColor);
            }
        }
    }
}

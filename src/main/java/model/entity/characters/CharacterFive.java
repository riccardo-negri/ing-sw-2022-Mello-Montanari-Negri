package model.entity.characters;

import model.entity.Game;
import model.entity.Island;
import model.entity.Wizard;

public class CharacterFive extends Character{

    private Integer cardNumber;

    public CharacterFive() {
        super(2);
        cardNumber = 4;
    }

    public void useEffect(Wizard player, Game game, Island island) throws Exception{
        if(cardNumber <= 0) throw new Exception("No more stop cards available");
        useCard(player);
        island.addStopCard(this);
    }

    public void removeCard() { cardNumber -= 1; }

    public void returnCard() { cardNumber += 1; }
}

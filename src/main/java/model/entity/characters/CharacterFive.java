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

    /**
     * a stop card can be put on any island to prevent a change of owner
     * @param player the player playing the card
     * @param game the current game
     * @param island the island to put the student on
     */
    public void useEffect(Wizard player, Game game, Island island) throws Exception{
        characterFiveValidator(player, game, island);
        useCard(player);
        island.addStopCard(this);
    }

    /**
     * Validator for character five useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @param island the island to put the student on
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * there are no more stop card available, or the island does not exist
     */
    public void characterFiveValidator(Wizard player, Game game, Island island) throws Exception {
        characterValidator(player, game);
        if(cardNumber <= 0) throw new Exception("No more stop cards available");
        if (game.getIslandGroupList().stream().flatMap(x -> x.getIslandList().stream()).noneMatch(x -> x == island))
            throw new Exception("Island does not exist");
    }

    public void removeOneCard() { cardNumber -= 1; }

    public void addOneCard() { cardNumber += 1; }
}

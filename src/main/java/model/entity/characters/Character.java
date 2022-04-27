package model.entity.characters;

import model.entity.Game;
import model.entity.bag.Bag;
import model.entity.Wizard;
import model.entity.gameState.ActionState;

public abstract class Character {

    protected Wizard activator;
    private final Integer price;
    private boolean used;

    public static Character generateCharacter(int number, Bag bag) {

        switch (number) {
            case 0: return new CharacterOne(bag);
            case 1: return new CharacterTwo();
            case 2: return new CharacterThree();
            case 3: return new CharacterFour();
            case 4: return new CharacterFive();
            case 5: return new CharacterSix();
            case 6: return new CharacterSeven(bag);
            case 7: return new CharacterEight();
            case 8: return new CharacterNine();
            case 9: return new CharacterTen();
            case 10: return new CharacterEleven(bag);
            case 11: return new CharacterTwelve();
            default: return null;
        }
    }

    protected Character(Integer price) {
        this.price = price;
        this.used = false;
    }

    protected void useCard(Wizard player) throws Exception{
        player.payEffect(price + (used ? 1 : 0));
        activator = player;
        used = true;
    }

    /**
     * Validator for all the character useEffect
     * @param player the player using the effect
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    protected void characterValidator (Wizard player, Game game) throws Exception {
        if (game.getGameState().getCurrentPlayer() != player)
            throw new Exception("Wrong player");
        if (price + (used ? 1 : 0) > player.getMoney())
            throw new Exception("Not enough money to activate the effect");
    }

    public Wizard getActivator() { return activator; }
}

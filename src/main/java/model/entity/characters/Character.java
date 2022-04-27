package model.entity.characters;

import model.entity.bag.Bag;
import model.entity.Wizard;

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
        if(used) player.payEffect(price+1);
        else player.payEffect(price);
        activator = player;
        used = true;
    }

    public Wizard getActivator() { return activator; }
}

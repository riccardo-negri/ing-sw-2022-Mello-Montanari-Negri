package it.polimi.ingsw.model.entity.characters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.bag.Bag;

import java.lang.reflect.Type;

public abstract class Character {

    protected final Integer characterId;
    protected Wizard activator;
    private final Integer price;
    private boolean used;

    public static Character generateCharacter(int number, Bag bag) {

        switch (number) {
            case 0: return new CharacterOne(1,bag);
            case 1: return new CharacterTwo(2);
            case 2: return new CharacterThree(3);
            case 3: return new CharacterFour(4);
            case 4: return new CharacterFive(5);
            case 5: return new CharacterSix(6);
            case 6: return new CharacterSeven(7,bag);
            case 7: return new CharacterEight(8);
            case 8: return new CharacterNine(9);
            case 9: return new CharacterTen(10);
            case 10: return new CharacterEleven(11,bag);
            case 11: return new CharacterTwelve(12);
            default: return null;
        }
    }

    protected Character(Integer characterId, Integer price) {
        this.characterId = characterId;
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

    public Integer getCharacterId() { return characterId; }
}

package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.enums.Tower;

public abstract class Character {

    protected final Integer gameId;
    protected final Integer characterId;
    protected Integer activator;
    private final Integer price;
    private boolean used;

    public static Character generateCharacter(int gameId, int number, Bag bag) {

        switch (number) {
            case 0: return new CharacterOne(gameId, 1,bag);
            case 1: return new CharacterTwo(gameId, 2);
            case 2: return new CharacterThree(gameId, 3);
            case 3: return new CharacterFour(gameId, 4);
            case 4: return new CharacterFive(gameId, 5);
            case 5: return new CharacterSix(gameId, 6);
            case 6: return new CharacterSeven(gameId, 7,bag);
            case 7: return new CharacterEight(gameId, 8);
            case 8: return new CharacterNine(gameId, 9);
            case 9: return new CharacterTen(gameId, 10);
            case 10: return new CharacterEleven(gameId, 11,bag);
            case 11: return new CharacterTwelve(gameId, 12);
            default: return null;
        }
    }

    protected Character(Integer gameId, Integer characterId, Integer price) {
        this.gameId = gameId;
        this.characterId = characterId;
        this.price = price;
        this.used = false;
    }

    protected void useCard(Integer playingWizard) throws Exception{
        Game.request(gameId).getWizard(playingWizard).payEffect(price + (used ? 1 : 0));
        activator = playingWizard;
        used = true;
    }

    /**
     * Validator for all the character useEffect
     * @param playingWizard the player using the effect
     * @throws Exception if it is not the player turn, or he does not have enough money to activate the card
     */
    protected void characterValidator (Integer playingWizard) throws Exception {
        if (Game.request(gameId).getGameState().getCurrentPlayer() != playingWizard)
            throw new Exception("Wrong player");
        if (price + (used ? 1 : 0) > Game.request(gameId).getWizard(playingWizard).getMoney())
            throw new Exception("Not enough money to activate the effect");
    }

    public Wizard getActivator() { return Game.request(gameId).getWizard(activator); }

    public Integer getCharacterId() { return characterId; }
}

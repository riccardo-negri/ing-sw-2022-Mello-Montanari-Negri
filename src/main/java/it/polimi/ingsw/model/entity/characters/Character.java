package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.Bag;

import java.util.Objects;

public abstract class Character {

    protected transient Integer gameId;
    protected final Integer characterId;
    protected Integer activator;
    private final Integer price;
    private boolean used;

    public static Character generateCharacter(int gameId, int number, Bag bag) {

        return switch (number) {
            case 1 -> new CharacterOne(gameId, 1, bag);
            case 2 -> new CharacterTwo(gameId, 2);
            case 3 -> new CharacterThree(gameId, 3);
            case 4 -> new CharacterFour(gameId, 4);
            case 5 -> new CharacterFive(gameId, 5);
            case 6 -> new CharacterSix(gameId, 6);
            case 7 -> new CharacterSeven(gameId, 7, bag);
            case 8 -> new CharacterEight(gameId, 8);
            case 9 -> new CharacterNine(gameId, 9);
            case 10 -> new CharacterTen(gameId, 10);
            case 11 -> new CharacterEleven(gameId, 11, bag);
            case 12 -> new CharacterTwelve(gameId, 12);
            default -> null;
        };
    }

    protected Character(Integer gameId, Integer characterId, Integer price) {
        this.gameId = gameId;
        this.characterId = characterId;
        this.price = price;
        this.used = false;
    }

    public void refreshGameId(Game game) { this.gameId = game.getId(); }

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
        if (!Objects.equals(Game.request(gameId).getGameState().getCurrentPlayer(), playingWizard))
            throw new Exception("Wrong player");
        if (price + (used ? 1 : 0) > Game.request(gameId).getWizard(playingWizard).getMoney())
            throw new Exception("Not enough money to activate the effect");
    }

    public Wizard getActivator() { return Game.request(gameId).getWizard(activator); }

    public Integer getId() { return characterId; }

    public int getPrize() { return price + (used ? 1 : 0); }
}

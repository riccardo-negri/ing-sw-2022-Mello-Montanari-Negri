package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.entity.gameState.ActionState;

import java.util.Objects;

public abstract class Character {

    protected transient Integer gameId;
    protected final Integer characterId;
    protected Integer activator;
    private final Integer price;
    private boolean used;

    /**
     * factory method to generate character
     * @param gameId id of the game
     * @param number id of the character
     * @param bag bag object for some characters who need it
     * @return returns the created object
     */
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

    /**
     * Super Constructor for charcaters, sets price and id
     * @param gameId id of the game
     * @param characterId id of the character
     * @param price cost of the character
     */
    protected Character(Integer gameId, Integer characterId, Integer price) {
        this.gameId = gameId;
        this.characterId = characterId;
        this.price = price;
        this.used = false;
    }

    /**
     * changes the gameId of the character
     * @param game game to get the id from
     */
    public void refreshGameId(Game game) { this.gameId = game.getId(); }

    /**
     * sets the activated character in the state and pays the price for the character
     * @param playingWizard player who is using the effect
     * @throws GameRuleException if the money is not enough
     */
    protected void useCard(Integer playingWizard) throws GameRuleException{
        Game.request(gameId).getWizard(playingWizard).payEffect(price + (used ? 1 : 0));
        ((ActionState) Game.request(gameId).getGameState()).activateEffect(this);
        activator = playingWizard;
        used = true;
    }

    /**
     * Validator for all the character useEffect
     * @param playingWizard the player using the effect
     * @throws GameRuleException if it is not the player turn, or he does not have enough money to activate the card
     */
    protected void characterValidator (Integer playingWizard) throws GameRuleException {
        if (!Objects.equals(Game.request(gameId).getGameState().getCurrentPlayer(), playingWizard))
            throw new GameRuleException("Wrong player");
        if(Objects.equals(Game.request(gameId).getGameState().getGameStateName(), "PS"))
            throw new GameRuleException("Wrong game phase");
        if(((ActionState) Game.request(gameId).getGameState()).getActivatedCharacter() != null)
            throw new GameRuleException("A character is already active");
        if (price + (used ? 1 : 0) > Game.request(gameId).getWizard(playingWizard).getMoney())
            throw new GameRuleException("Not enough money to activate the effect");
    }

    public Wizard getActivator() { return Game.request(gameId).getWizard(activator); }

    public Integer getId() { return characterId; }

    public int getPrize() { return price + (used ? 1 : 0); }
}

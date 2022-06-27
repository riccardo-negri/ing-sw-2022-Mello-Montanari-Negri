package it.polimi.ingsw.model.entity.game_state;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Cloud;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;

import java.util.*;
import java.util.stream.Collectors;

public class PlanningState extends GameState {

    /**
     * Constructor to be used for the first creation of a gameState, at the beginning of the game, it also fills the clouds
     * @param playerList The list of wizard of the game
     * @param randomGenerator a random generator to shuffle the wizards, to choose the order of execution of the first planning phase randomly
     */
    public PlanningState (Integer gameId, List<Integer> playerList, Random randomGenerator) {
        super(playerList, gameId);
        gameState = "PS";

        int playerNumber = playerList.size();
        int firstPlayer = randomGenerator.nextInt(playerNumber);
        playerOrder = new ArrayList<>();
        for (int i=0; i<playerNumber; i++)
            playerOrder.add((firstPlayer + i) % playerNumber);

        for(Cloud c : Game.request(gameId).getCloudList()) c.fillCloud();

        Game.request(gameId);


    }

    /**
     * Constructor to be used for all the successive planning states, it also fills the clouds
     * @param oldState the previous state, an ActionState
     */
    public PlanningState(GameState oldState) {
        super(oldState);

        this.currentlyPlaying = 0;

        int playerNumber = oldState.playerOrder.size();
        int firstPlayer = oldState.playerOrder.get(0);
        playerOrder = new ArrayList<>();
        for (int i=0; i<playerNumber; i++)
            playerOrder.add((firstPlayer + i) % playerNumber);

        Game game = Game.request(gameId);

        if (game.getBag().isEmpty() || !game.getWizard(0).getCardDeck().checkAvailableCards(new ArrayList<>()))
            game.endGame();

        gameState = "PS";
        for(Cloud c : game.getCloudList()) c.fillCloud();

        for (Integer playerId : playerOrder)
            game.getWizard(playerId).getCardDeck().removeCurrentCard();


    }

    /**
     * Selection of the assistant card to be played
     * @param playingWizard the player who is selecting the card
     * @param selected the number of the card to select (1 to 10)
     * @throws GameRuleException wrong player according to the turns, or the card already played
     */
    public void selectCard (Integer playingWizard, Integer selected) throws GameRuleException {
        cardSelectionValidator(playingWizard, selected);
        Game.request(gameId).getWizard(playingWizard).getCardDeck().playCard(selected);
        currentlyPlaying++;
        checkState();
    }

    /**
     * Validation of card selection by player
     * @param playingWizard the player who is selecting the card
     * @param selected the number of the card to select (1 to 10)
     * @throws GameRuleException wrong player according to the turns, or the card already played
     */
    public void cardSelectionValidator(Integer playingWizard, Integer selected) throws GameRuleException {

        if (!Objects.equals(playingWizard, playerOrder.get(currentlyPlaying))) throw new GameRuleException("Wrong player");
        if (!Objects.equals(gameState, "PS")) throw new GameRuleException("Wrong game phase");

        Wizard player = Game.request(gameId).getWizard(playingWizard);

        if (Arrays.stream(player.getCardDeck().getDeckCards()).noneMatch(x -> x==selected)) throw new GameRuleException("Card not available");

        List<Integer> cardNumbers = new ArrayList<>();
        for (int i=0; i<currentlyPlaying; i++)
            cardNumbers.add(Game.request(gameId).getWizard(playerOrder.get(i)).getCardDeck().getCurrentCard().getNumber());
        if (cardNumbers.contains(selected) && player.getCardDeck().checkAvailableCards(cardNumbers))
            throw new GameRuleException("Card already played with valid alternatives");
    }

    /**
     * checks if all the players chose their assistant card already
     */
    private void checkState() {
        if (currentlyPlaying == playerOrder.size()) {
            playerOrder = playerOrder.stream().sorted((x, y) ->
                Integer.compare(Game.request(gameId).getWizard(x).getCardDeck().getCurrentCard().getNumber(), Game.request(gameId).getWizard(y).getCardDeck().getCurrentCard().getNumber())).collect(Collectors.toList());
            Game.request(gameId).updateGameState(new MoveStudentActionState(this));
        }
    }
}

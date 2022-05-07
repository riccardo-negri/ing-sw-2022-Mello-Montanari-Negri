package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Cloud;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.Tower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlanningState extends GameState {

    /**
     * Constructor to be used for the first creation of a gameState, at the beginning of the game, it also fills the clouds
     * @param towerList The list of wizard of the game
     * @param randomGenerator a random generator to shuffle the wizards, to choose the order of execution of the first planning phase randomly
     */
    public PlanningState (Integer gameId, List<Tower> towerList, Random randomGenerator) {
        super(towerList, gameId);
        gameState = "PS";
        Collections.shuffle(towerOrder, randomGenerator);
        try { for(Cloud c : Game.request(gameId).getCloudList()) c.fillCloud();
        } catch (Exception e) {
            //TODO codice per gestire la fine degli studenti nella bag, condizione di fine partita
        }
    }

    /**
     * Constructor to be used for all the successive planning states, it also fills the clouds
     * @param oldState the previous state, an ActionState
     */
    public PlanningState(GameState oldState) {
        super(oldState);
        gameState = "PS";
        try { for(Cloud c : Game.request(gameId).getCloudList()) c.fillCloud();
        } catch (Exception e) {
            //TODO codice per gestire la fine degli studenti nella bag, condizione di fine partita
        }
    }

    /**
     * Selection of the assistant card to be played
     * @param playingTower the player who is selecting the card
     * @param selected the number of the card to select (1 to 10)
     * @throws Exception wrong player according to the turns, or the card already played
     */
    public void selectCard (Tower playingTower, Integer selected) throws Exception {
        cardSelectionValidator(playingTower, selected);
        Game.request(gameId).getWizard(playingTower).getCardDeck().playCard(selected);
        currentlyPlaying++;
        checkState();
    }

    /**
     * Validation of card selection by player
     * @param playingTower the player who is selecting the card
     * @param selected the number of the card to select (1 to 10)
     * @throws Exception wrong player according to the turns, or the card already played
     */
    public void cardSelectionValidator(Tower playingTower, Integer selected) throws Exception {

        if (playingTower != towerOrder.get(currentlyPlaying)) throw new Exception("Wrong player");

        Wizard player = Game.request(gameId).getWizard(playingTower);

        List<Integer> cardNumbers = new ArrayList<>();
        for (int i=0; i<currentlyPlaying; i++)
            cardNumbers.add(player.getCardDeck().getCurrentCard().getNumber());
        if (cardNumbers.contains(selected) && player.getCardDeck().checkAvailableCards(cardNumbers))
            throw new Exception("Card already played with valid alternatives");
    }

    /**
     * checks if all the players chose their assistant card already
     */
    private void checkState() {
        if (currentlyPlaying == towerOrder.size()) {
            towerOrder.stream().sorted((x, y) -> {
                int valX = Game.request(gameId).getWizard(x).getCardDeck().getCurrentCard().getNumber();
                int valY = Game.request(gameId).getWizard(y).getCardDeck().getCurrentCard().getNumber();
                if(valX > valY) return 1;
                else if (valX < valY) return -1;
                else return 0;
            });
            Game.request(gameId).updateGameState(new MoveStudentActionState(this));
        }
    }
}

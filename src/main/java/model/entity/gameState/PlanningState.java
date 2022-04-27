package model.entity.gameState;

import model.entity.Cloud;
import model.entity.Game;
import model.entity.Wizard;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PlanningState extends GameState {

    /**
     * Constructor to be used for the first creation of a gameState, at the beginning of the game, it also fills the clouds
     * @param wizardList The list of wizard of the game
     * @param randomGenerator a random generator to shuffle the wizards, to choose the order of execution of the first planning phase randomly
     */
    public PlanningState (Game game, List<Wizard> wizardList, Random randomGenerator) {
        super(wizardList, game);
        Collections.shuffle(order, randomGenerator);
        try {
            for(Cloud c : game.getCloudList()) c.fillCloud();
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
        try {
            for(Cloud c : game.getCloudList()) c.fillCloud();
        } catch (Exception e) {
            //TODO codice per gestire la fine degli studenti nella bag, condizione di fine partita
        }
    }

    /**
     * Selection of the assistant card to be played
     * @param player the player who is selectiong the cart
     * @param selected the number of the card to select (1 to 10)
     * @throws Exception wrong player according to the turns, or the card already played
     */
    public void selectCard (Wizard player, Integer selected) throws Exception {

        //TODO da rivedere questa classe che non mi sembra abbia molto senso

        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");

        order.get(currentlyPlaying).getCardDeck().playCard(selected);

        currentlyPlaying++;

        if (currentlyPlaying == order.size()) {
            order.stream().sorted((x,y) -> {
                int valX = x.getCardDeck().getCurrentCard().getNumber();
                int valY = y.getCardDeck().getCurrentCard().getNumber();
                if(valX > valY) return 1;
                else if (valX < valY) return -1;
                else return 0;
            });

            game.updateGameState(new MoveStudentActionState(this));
        }
    }
}

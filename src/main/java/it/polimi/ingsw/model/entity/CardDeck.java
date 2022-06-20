package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.AssistantCard;

import java.util.ArrayList;
import java.util.List;

/**
 * list of the available assistant cards
 */
public class CardDeck {

    private final List<AssistantCard> assistantCards;
    private AssistantCard currentCard;

    /**
     * generates the deck of ten card, from 1 to 10
     */
    public CardDeck() {
        this.assistantCards = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            this.assistantCards.add(AssistantCard.getFromValue(i));
        }
    }

    /**
     * to play card during the planning phase, it automatically remove the card from the deck
     * @param cardNumber the number of the card to be played
     * @throws GameRuleException if the card does not have an accepted number or is already played
     */
    public void playCard (Integer cardNumber) throws GameRuleException {
        List<AssistantCard> card = assistantCards.stream().filter(x -> x.getNumber().equals(cardNumber)).toList();
        if (card.isEmpty()) throw new GameRuleException("Card not available");
        currentCard = card.get(0);
        assistantCards.remove(currentCard);
    }

    /**
     * checks if there is at least a card available to be used without playing the opponent card
     * @param cards cards already played by the opponents
     * @return if there is at least an available card
     */
    public boolean checkAvailableCards(List<Integer> cards) {
        return assistantCards.stream().map(AssistantCard::getNumber).anyMatch(x -> ! cards.contains(x));
    }

    public AssistantCard getCurrentCard() { return currentCard; }

    public void removeCurrentCard() { currentCard = null; }

    /**
     * @return returns all the still available assistant cards
     */
    public int[] getDeckCards() {
        int[] cards = new int[assistantCards.size()];
        for (int i = 0; i < assistantCards.size(); i++) {
            cards[i] = assistantCards.get(i).getNumber();
        }
        return  cards;
    }
}

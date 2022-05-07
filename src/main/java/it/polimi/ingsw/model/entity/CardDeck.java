package it.polimi.ingsw.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @throws Exception if the card does not have an accepted number or is already played
     */
    public void playCard (Integer cardNumber) throws Exception {
        List<AssistantCard> card = assistantCards.stream().filter(x -> x.getNumber().equals(cardNumber)).collect(Collectors.toList());
        if (card.isEmpty()) throw new Exception("Card not available");
        currentCard = card.get(0);
        assistantCards.remove(currentCard);
    }

    public boolean checkAvailableCards(List<Integer> cards) {
        return assistantCards.stream().map(AssistantCard::getNumber).anyMatch(x -> ! cards.contains(x));
    }

    public AssistantCard getCurrentCard() { return currentCard; }
}

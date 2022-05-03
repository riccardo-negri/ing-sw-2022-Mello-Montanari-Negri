package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.PlanningState;

public class CardChoice extends Move{
    private final int card;

    public CardChoice(int card) {
        this.card = card;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((PlanningState) game.getGameState()).selectCard(wizard.getTowerColor(), card);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((PlanningState) game.getGameState()).cardSelectionValidator(wizard.getTowerColor(), card);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    public int getCard() {
        return card;
    }
}

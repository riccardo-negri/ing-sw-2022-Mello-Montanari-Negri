package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.PlanningState;

public class CardChoice extends Move{
    private final int card;

    public CardChoice(Wizard author, int card) {
        super(author);
        this.card = card;
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((PlanningState) game.getGameState()).selectCard(authorId, card);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        try {
            ((PlanningState) game.getGameState()).cardSelectionValidator(authorId, card);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    public int getCard() {
        return card;
    }
}

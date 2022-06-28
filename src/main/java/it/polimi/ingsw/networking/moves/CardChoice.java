package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.game_state.PlanningState;

/**
 * represents the move of playing an assistant card in the planning phase
 * the number of the assistant card played
 */
public class CardChoice extends Move{
    private final int card;

    /**
     * create a message requesting to play an assistant card
     * @param author the wizard playing the card
     * @param card the number of the assistant card
     */
    public CardChoice(Wizard author, int card) {
        super(author);
        this.card = card;
    }

    /**
     * change the game state according to the card choice effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((PlanningState) game.getGameState()).selectCard(authorId, card);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * check the validity of the card choice without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        try {
            ((PlanningState) game.getGameState()).cardSelectionValidator(authorId, card);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * get card value
     * @return the number of the assistant card
     */
    public int getCard() {
        return card;
    }
}

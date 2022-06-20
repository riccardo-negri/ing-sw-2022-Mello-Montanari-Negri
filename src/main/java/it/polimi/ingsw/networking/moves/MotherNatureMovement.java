package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;

public class MotherNatureMovement extends Move{
    private final int steps;

    /**
     * create a message requesting to move mother nature
     * @param author the id of the wizard who required the move
     * @param steps the number of island groups mother nature moves
     */
    public MotherNatureMovement(Wizard author, int steps) {
        super(author);
        this.steps = steps;
    }

    /**
     * change the game state according to the movement effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(authorId, steps);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * check the validity of the movement without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNatureValidator(authorId, steps);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * get steps value
     * @return the number of island groups mother nature moves
     */
    public int getSteps() {
        return steps;
    }
}

package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;

public class MotherNatureMovement extends Move{
    private final int steps;

    public MotherNatureMovement(Wizard author, int steps) {
        super(author);
        this.steps = steps;
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(authorId, steps);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game) throws Exception {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNatureValidator(authorId, steps);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    public int getSteps() {
        return steps;
    }
}

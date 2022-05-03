package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;

public class MotherNatureMovement extends Move{
    private final int steps;

    public MotherNatureMovement(int steps) {
        this.steps = steps;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(wizard.getTowerColor(), steps);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNatureValidator(wizard.getTowerColor(), steps);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }
}

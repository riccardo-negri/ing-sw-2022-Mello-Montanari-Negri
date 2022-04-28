package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;

public class MoveMotherNature extends Move{
    private final int steps;

    public MoveMotherNature(int steps) {
        this.steps = steps;
    }

    @Override
    public void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(wizard, steps);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNatureValidator(wizard, steps);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }
}

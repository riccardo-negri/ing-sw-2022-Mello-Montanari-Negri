package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveStudentActionState;

public class DiningRoomMovement extends Move{
    private final StudentColor color;

    public DiningRoomMovement(StudentColor color) {
        this.color = color;
    }

    @Override
    public void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(wizard, color);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoomValidato(wizard, color);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }
}

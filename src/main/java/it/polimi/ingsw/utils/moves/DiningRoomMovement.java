package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveStudentActionState;
import it.polimi.ingsw.model.enums.StudentColor;

public class DiningRoomMovement extends Move{
    private final StudentColor student;

    public DiningRoomMovement(StudentColor student) {
        this.student = student;
    }

    @Override
    public void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(wizard.getId(), student);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoomValidator(wizard.getId(), student);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }
}

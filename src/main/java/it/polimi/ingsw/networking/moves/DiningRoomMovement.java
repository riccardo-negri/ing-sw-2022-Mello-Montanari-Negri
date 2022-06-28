package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.game_state.MoveStudentActionState;
import it.polimi.ingsw.model.enums.StudentColor;

/**
 * represents the move of moving a student from the entrance into the dining room
 * contains the color of the student to move
 */
public class DiningRoomMovement extends Move{
    private final StudentColor student;

    /**
     * create a message requesting to move a student from the entrance to the dining room
     * @param author author the id of the wizard who required the move
     * @param student the color of the student that is moved
     */
    public DiningRoomMovement(Wizard author, StudentColor student) {
        super(author);
        this.student = student;
    }

    /**
     * change the game state according to the movement effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(authorId, student);
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
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoomValidator(authorId, student);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * get student value
     * @return the color of the student that is moved
     */
    public StudentColor getStudent() {
        return student;
    }
}

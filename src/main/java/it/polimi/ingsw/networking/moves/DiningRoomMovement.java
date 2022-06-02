package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveStudentActionState;
import it.polimi.ingsw.model.enums.StudentColor;

public class DiningRoomMovement extends Move{
    private final StudentColor student;

    public DiningRoomMovement(Wizard author, StudentColor student) {
        super(author);
        this.student = student;
    }

    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(authorId, student);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game) throws GameRuleException {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoomValidator(authorId, student);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    public StudentColor getStudent() {
        return student;
    }
}

package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveStudentActionState;
import it.polimi.ingsw.model.enums.StudentColor;

public class IslandMovement extends Move{
    private final StudentColor student;
    private final int islandId;

    /**
     * create a message requesting to move a student from the entrance to an island
     * @param author author the id of the wizard who required the move
     * @param student the color of the student that is moved
     * @param islandId the id of the island where the student is moved
     */
    public IslandMovement(Wizard author, StudentColor student, int islandId) {
        super(author);
        this.student = student;
        this.islandId = islandId;
    }

    /**
     * change the game state according to the movement effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(authorId, student, islandId);
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
            ((MoveStudentActionState) game.getGameState()).moveStudentToIslandValidator(authorId, student, islandId);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * get islandId value
     * @return the id of the island where the student is moved
     */
    public int getIslandId() {
        return islandId;
    }

    /**
     * get student value
     * @return the color of the student that is moved
     */
    public StudentColor getStudent() {
        return student;
    }
}

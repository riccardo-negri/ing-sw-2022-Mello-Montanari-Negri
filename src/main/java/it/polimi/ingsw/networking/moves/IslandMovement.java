package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveStudentActionState;
import it.polimi.ingsw.model.enums.StudentColor;

public class IslandMovement extends Move{
    private final StudentColor student;
    private final int islandId;

    public IslandMovement(Wizard author, StudentColor student, int islandId) {
        super(author);
        this.student = student;
        this.islandId = islandId;
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(authorId, student, islandId);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIslandValidator(authorId, student, islandId);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    public int getIslandId() {
        return islandId;
    }

    public StudentColor getStudent() {
        return student;
    }
}

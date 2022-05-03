package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.MoveStudentActionState;
import it.polimi.ingsw.model.enums.StudentColor;

public class IslandMovement extends Move{
    private final StudentColor student;
    private final int islandId;

    public IslandMovement(StudentColor student, int islandId) {
        this.student = student;
        this.islandId = islandId;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(wizard.getTowerColor(), student, islandId);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIslandValidator(wizard.getTowerColor(), student, islandId);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }
}

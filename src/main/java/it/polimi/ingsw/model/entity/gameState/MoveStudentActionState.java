package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.Objects;

public class MoveStudentActionState extends ActionState {

    private Integer studentMoved;

    protected MoveStudentActionState(GameState oldState){
        super(oldState);
        gameState = "MSS";
        studentMoved = 0;
    }

    /**
     * First phase of the Action State, to move a student to the dining room from the entrance
     * @param playingWizard the player doing the move
     * @param studentColor the color of student to be moved
     * @throws GameRuleException in case of impossible movement or wrong player
     */
    public void moveStudentToDiningRoom (Integer playingWizard, StudentColor studentColor) throws GameRuleException {
        moveStudentToDiningRoomValidator(playingWizard, studentColor);
        takeStudent(playingWizard, studentColor);
        Game.request(gameId).getWizard(playingWizard).putDiningStudent(studentColor);
        Game.request(gameId).getProfessor(studentColor).refreshMaster(playingWizard);
    }

    /**
     * Validator for moveStudentToDiningRoom method
     * @param playingWizard the player doing the move
     * @param studentColor the color of student to be moved
     * @throws GameRuleException if it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    public void moveStudentToDiningRoomValidator(Integer playingWizard, StudentColor studentColor) throws GameRuleException {
        moveStudentValidator(playingWizard, studentColor);
        Game.request(gameId).getWizard(playingWizard).checkDiningStudentNUmber(studentColor);
    }


    /**
     * First phase of the Action State, to move a student to an island from the entrance
     * @param playingWizard the player doing the move
     * @param studentColor the color of student to be moved
     * @param islandId the destination island
     */
    public void moveStudentToIsland (Integer playingWizard, StudentColor studentColor, Integer islandId) throws GameRuleException {
        moveStudentToIslandValidator(playingWizard, studentColor, islandId);
        takeStudent(playingWizard, studentColor);
        Game.request(gameId).getIsland(islandId).putIslandStudent(studentColor);
    }

    /**
     * Validator for moveStudentToIsland method
     * @param playingWizard the player doing the move
     * @param studentColor the color of student to be moved
     * @param islandId the destination island
     * @throws GameRuleException if the island doesn't exist, it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    public void moveStudentToIslandValidator(Integer playingWizard, StudentColor studentColor, Integer islandId) throws GameRuleException {
        moveStudentValidator(playingWizard, studentColor);
        if (islandId<0 || islandId>=12) throw new GameRuleException("Island not found");
    }

    /**
     * Inner method to take a student from the entrance if present
     * @param playingWizard the player doing the move
     * @param studentColor the color of the student to be moved
     */
    private void takeStudent (Integer playingWizard, StudentColor studentColor) {
        Game.request(gameId).getWizard(playingWizard).takeEntranceStudent(studentColor);
        studentMoved++;
        updatePhase();
    }

    /**
     * method to check if it is the right time to call the method,
     * @param playingWizard the player asking for the move
     * @param studentColor the color of the student to be moved
     * @throws GameRuleException if it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    private void moveStudentValidator(Integer playingWizard, StudentColor studentColor) throws GameRuleException {
        if (!Objects.equals(playingWizard, playerOrder.get(currentlyPlaying)))
            throw new GameRuleException("Wrong player");
        if (!Objects.equals(gameState, "MSS"))
            throw new GameRuleException("Wrong game phase");
        if (!Game.request(gameId).getWizard(playingWizard).getEntranceStudents().contains(studentColor))
            throw new GameRuleException("No students available in the selected color");
    }

    /**
     * It checks if all the students are moved and the game can move to the next phase
     */
    private void updatePhase() {
        if(studentMoved.equals(Game.request(gameId).getPlayerNumber().getCloudSize()))
            Game.request(gameId).updateGameState(new MoveMotherNatureActionState(this));
    }
}

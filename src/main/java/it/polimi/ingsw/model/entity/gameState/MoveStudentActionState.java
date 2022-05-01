package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Island;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;
import java.util.stream.Collectors;

public class MoveStudentActionState extends ActionState {

    private Integer studentMoved;

    protected MoveStudentActionState(GameState oldState){
        super(oldState);
        gameState = "MSS";
        studentMoved = 0;
    }

    /**
     * First phase of the Action State, to move a student to the dining room from the entrance
     * @param playingTower the player doing the move
     * @param studentColor the color of student to be moved
     * @throws Exception in case of impossible movement or wrong player
     */
    public void moveStudentToDiningRoom (Tower playingTower, StudentColor studentColor) throws Exception {
        moveStudentToDiningRoomValidator(playingTower, studentColor);
        takeStudent(playingTower, studentColor);
        Game.request(gameId).getWizard(playingTower).putDiningStudent(studentColor);
        Game.request(gameId).getProfessor(studentColor).refreshMaster(playingTower);
    }

    /**
     * Validator for moveStudentToDiningRoom method
     * @param playingTower the player doing the move
     * @param studentColor the color of student to be moved
     * @throws Exception if it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    public void moveStudentToDiningRoomValidator(Tower playingTower, StudentColor studentColor) throws Exception {
        moveStudentValidator(playingTower, studentColor);
    }


    /**
     * First phase of the Action State, to move a student to an island from the entrance
     * @param playingTower the player doing the move
     * @param studentColor the color of student to be moved
     * @param islandId the destination island
     */
    public void moveStudentToIsland (Tower playingTower, StudentColor studentColor, Integer islandId) throws Exception {
        moveStudentToIslandValidator(playingTower, studentColor, islandId);
        takeStudent(playingTower, studentColor);
        Game.request(gameId).getIsland(islandId).putIslandStudent(studentColor);
    }

    /**
     * Validator for moveStudentToIsland method
     * @param playingTower the player doing the move
     * @param studentColor the color of student to be moved
     * @param islandId the destination island
     * @throws Exception if the island doesn't exist, it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    public void moveStudentToIslandValidator(Tower playingTower, StudentColor studentColor, Integer islandId) throws Exception {
        moveStudentValidator(playingTower, studentColor);
        if (islandId<0 || islandId>=12) throw new Exception("Island not found");
    }

    /**
     * Inner method to take a student from the entrance if present
     * @param playingTower the player doing the move
     * @param studentColor the color of the student to be moved
     */
    private void takeStudent (Tower playingTower, StudentColor studentColor) {
        Game.request(gameId).getWizard(playingTower).takeEntranceStudent(studentColor);
        studentMoved++;
        updatePhase();
    }

    /**
     * method to check if it is the right time to call the method,
     * @param playingTower the player asking for the move
     * @param studentColor the color of the student to be moved
     * @throws Exception if it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    private void moveStudentValidator(Tower playingTower, StudentColor studentColor) throws Exception {
        if (playingTower != towerOrder.get(currentlyPlaying))
            throw new Exception("Wrong player");
        if (studentMoved >= Game.request(gameId).getPlayerNumber().getCloudSize())
            throw new Exception("All students already moved");
        if (!Game.request(gameId).getWizard(playingTower).getEntranceStudents().contains(studentColor))
            throw new Exception("No students available in the selected color");
    }

    /**
     * It checks if all the students are moved and the game can move to the next phase
     */
    private void updatePhase() {
        if(studentMoved.equals(Game.request(gameId).getPlayerNumber().getCloudSize()))
            Game.request(gameId).updateGameState(new MoveMotherNatureActionState(this));
    }
}

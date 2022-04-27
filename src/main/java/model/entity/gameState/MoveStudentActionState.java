package model.entity.gameState;

import model.entity.Island;
import model.entity.Wizard;
import model.enums.StudentColor;

import java.util.List;
import java.util.stream.Collectors;

public class MoveStudentActionState extends ActionState {

    private Integer studentMoved;

    protected MoveStudentActionState(GameState oldState){
        super(oldState);
        studentMoved = 0;
    }

    /**
     * First phase of the Action State, to move a student to the dining room from the entrance
     * @param player the player doing the move
     * @param studentColor the color of student to be moved
     * @throws Exception in case of impossible movement or wrong player
     */
    public void moveStudentToDiningRoom (Wizard player, StudentColor studentColor) throws Exception {
        moveStudentToDiningRoomValidator(player, studentColor);
        takeStudent(player, studentColor);
        player.putDiningStudent(studentColor);
        game.getProfessor(studentColor).refreshMaster(player);
    }

    /**
     * Validator for moveStudentToDiningRoom method
     * @param player the player doing the move
     * @param studentColor the color of student to be moved
     * @throws Exception if it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    public void moveStudentToDiningRoomValidator(Wizard player, StudentColor studentColor) throws Exception {
        moveStudentValidator(player, studentColor);
    }


    /**
     * First phase of the Action State, to move a student to an island from the entrance
     * @param player the player doing the move
     * @param studentColor the color of student to be moved
     * @param island the destination island
     */
    public void moveStudentToIsland (Wizard player, StudentColor studentColor, Island island) throws Exception {
        moveStudentToIslandValidator(player, studentColor, island);
        takeStudent(player, studentColor);
        game.getIslandGroupList().stream()
                .flatMap(x -> x.getIslandList().stream())
                .filter(x -> x == island)
                .forEach(x -> x.putIslandStudent(studentColor));
    }

    /**
     * Validator for moveStudentToIsland method
     * @param player the player doing the move
     * @param studentColor the color of student to be moved
     * @param island the destination island
     * @throws Exception if the island doesn't exist, it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    public void moveStudentToIslandValidator(Wizard player, StudentColor studentColor, Island island) throws Exception {
        moveStudentValidator(player, studentColor);
        List<Island> found = game.getIslandGroupList().stream()
                .flatMap(x -> x.getIslandList().stream())
                .filter(x -> x == island).collect(Collectors.toList());
        if (found.isEmpty()) throw new Exception("Island not found");
    }

    /**
     * Inner method to take a student from the entrance if present
     * @param player the player doing the move
     * @param studentColor the color of the student to be moved
     */
    private void takeStudent (Wizard player, StudentColor studentColor) {
        player.takeEntranceStudent(studentColor);
        studentMoved++;
        updatePhase();
    }

    /**
     * method to check if it is the right time to call the method,
     * @param player the player asking for the move
     * @param studentColor the color of the student to be moved
     * @throws Exception if it is not the player turn to move students,
     * he moved too many students already or the student is not present
     */
    private void moveStudentValidator(Wizard player, StudentColor studentColor) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (studentMoved >= game.getPlayerNumber().getCloudSize()) throw new Exception("All students already moved");
        if (!player.getEntranceStudents().contains(studentColor)) throw new Exception("No students available in the selected color");
    }

    /**
     * It checks if all the students are moved and the game can move to the next phase
     */
    private void updatePhase() {
        if(studentMoved.equals(game.getPlayerNumber().getCloudSize()))
            game.updateGameState(new MoveMotherNatureActionState(this));
    }
}

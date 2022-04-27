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
        takeStudent(player, studentColor);
        player.putDiningStudent(studentColor);
        game.getProfessor(studentColor).refreshMaster(player);
    }

    /**
     * First phase of the Action State, to move a student to an island from the entrance
     * @param player the player doing the move
     * @param studentColor the color of student to be moved
     * @param island the destination island
     * @throws Exception in case of impossible movement or wrong player
     */
    public void moveStudentToIsland (Wizard player, StudentColor studentColor, Island island) throws Exception {
        List<Island> found = game.getIslandGroupList().stream()
                .flatMap(x -> x.getIslandList().stream())
                .filter(x -> x == island).collect(Collectors.toList());
        if (found.isEmpty()) throw new Exception("Island not found");
        takeStudent(player, studentColor);
        found.get(0).putIslandStudent(studentColor);
    }

    /**
     * Inner method to take a student from the entrance if present
     * @param player the player doing the move
     * @param studentColor the color of the student to be moved
     * @throws Exception if the student is not present
     */
    private void takeStudent (Wizard player, StudentColor studentColor) throws Exception {
        checkConditionsPhaseZero(player);
        if (player.takeEntranceStudent(studentColor)) {
            studentMoved++;
            updatePhase();
        } else throw new Exception("No students available in the selected color");
    }

    /**
     * method to check if it is the right time to call the method,
     * @param player the player asking for the move
     * @throws Exception if it is not the player turn to move students or he moved too many students already
     */
    private void checkConditionsPhaseZero(Wizard player) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (studentMoved >= game.getPlayerNumber().getCloudSize()) throw new Exception("All students already moved");
    }

    void updatePhase() {
        if(studentMoved.equals(game.getPlayerNumber().getCloudSize()))
            game.updateGameState(new MoveMotherNatureActionState(this));
    }
}

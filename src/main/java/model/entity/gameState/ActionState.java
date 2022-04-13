package model.entity.gameState;

import model.entity.Cloud;
import model.entity.Island;
import model.entity.Wizard;
import model.enums.StudentColor;

import java.util.List;
import java.util.stream.Collectors;

public class ActionState extends GameState {

    private Integer phase;
    private Integer studentMoved;

    public ActionState(GameState oldState) {
        super(oldState);
        phase = 0;
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
     * @throws Exception if it is not the player turn to move student or he moved too many students already
     */
    private void checkConditionsPhaseZero(Wizard player) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (phase != 0) throw new Exception("Wrong game phase, not phase MoveStudents");
        if (studentMoved >= game.getPlayerNumber().getCloudSize()) throw new Exception("All students already moved");
    }

    void updatePhase() {
        if(studentMoved.equals(game.getPlayerNumber().getCloudSize())) phase = 1;
    }

    /**
     * Second phase of the Action State, to move mother nature
     * @param player the player doing the move
     * @param steps the number of steps to move
     * @throws Exception wrong player, phase or too many steps asked
     */
    void moveMotherNature(Wizard player, Integer steps) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (phase != 1) throw new Exception("Wrong game phase, not phase Move Mother Nature");
        if (steps > player.getCardDeck().getCurrentCard().getSteps()) throw new Exception("Too many steps");
        game.doMotherNatureSteps(steps);
        game.getFistIslandGroup().updateTower(game);
        game.unifyIslands();
        phase = 2;
    }

    /**
     * Third phase of the Action State, to take a cloud
     * @param player the player doing the action
     * @param chosen the chosen cloud
     * @throws Exception wrong player, phase or cloud not available or taken
     */
    void chooseCloud(Wizard player, Cloud chosen) throws Exception{
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (phase != 1) throw new Exception("Wrong game phase, not phase Choose Cloud");
        if (!game.getCloudList().contains(chosen)) throw new Exception("Chosen cloud not available");
        try {
            player.getEntranceStudents().addAll(chosen.takeStudents());
        } catch (Exception e) { throw e; }

        if(++currentlyPlaying == order.size()) {
            game.updateGameState(new PlanningState(this));
        }
    }
}

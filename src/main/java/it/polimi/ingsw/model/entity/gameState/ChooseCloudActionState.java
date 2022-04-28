package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Cloud;
import it.polimi.ingsw.model.entity.Wizard;

public class ChooseCloudActionState extends ActionState{

    protected ChooseCloudActionState(GameState oldState){
        super(oldState);
    }

    /**
     * Third phase of the Action State, to take a cloud
     * @param player the player doing the action
     * @param chosen the chosen cloud
     */
    public void chooseCloud(Wizard player, Cloud chosen) throws Exception{
        chooseCloudValidator(player, chosen);
        player.getEntranceStudents().addAll(chosen.takeStudents());
        updateGameState();
    }

    /**
     * Validator for chooseCloud method
     * @param player the player doing the action
     * @param chosen the chosen cloud
     * @throws Exception wrong player, phase or cloud not available or taken
     */
    public void chooseCloudValidator(Wizard player, Cloud chosen) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (!game.getCloudList().contains(chosen)) throw new Exception("Chosen cloud not available");
    }

    /**
     * It moves to the next state of the game
     */
    private void updateGameState() {
        if(++currentlyPlaying == order.size()) game.updateGameState(new PlanningState(this));
        else game.updateGameState(new MoveStudentActionState(this));
    }
}

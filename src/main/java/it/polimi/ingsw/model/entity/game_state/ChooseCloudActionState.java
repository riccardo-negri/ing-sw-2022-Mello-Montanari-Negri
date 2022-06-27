package it.polimi.ingsw.model.entity.game_state;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;

import java.util.Objects;

public class ChooseCloudActionState extends ActionState{

    protected ChooseCloudActionState(GameState oldState){
        super(oldState);
        gameState = "CCS";
    }

    /**
     * Third phase of the Action State, to take a cloud
     * @param playingWizard the player doing the action
     * @param cloudId the chosen cloud
     */
    public void chooseCloud(Integer playingWizard, Integer cloudId) throws GameRuleException {
        chooseCloudValidator(playingWizard, cloudId);
        Game game = Game.request(gameId);
        game.getWizard(playingWizard).addEntranceStudents(game.getCloud(cloudId).takeStudents());
        updateGameState();
    }

    /**
     * Validator for chooseCloud method
     * @param playingWizard the player doing the action
     * @param cloudId the chosen cloud
     * @throws GameRuleException wrong player, phase or cloud not available or taken
     */
    public void chooseCloudValidator(Integer playingWizard, Integer cloudId) throws GameRuleException {
        if (!Objects.equals(playingWizard, playerOrder.get(currentlyPlaying))) throw new GameRuleException("Wrong player");
        if (!Objects.equals(gameState, "CCS")) throw new GameRuleException("Wrong game phase");

        if (cloudId >= Game.request(gameId).getPlayerNumber().getWizardNumber()) throw new GameRuleException("Chosen cloud not present");
        if (Game.request(gameId).getCloud(cloudId).isTaken()) throw new GameRuleException("Chosen cloud already taken");
    }

    /**
     * It moves to the next state of the game
     */
    private void updateGameState() {
        if(++currentlyPlaying == playerOrder.size()) Game.request(gameId).updateGameState(new PlanningState(this));
        else Game.request(gameId).updateGameState(new MoveStudentActionState(this));
    }
}

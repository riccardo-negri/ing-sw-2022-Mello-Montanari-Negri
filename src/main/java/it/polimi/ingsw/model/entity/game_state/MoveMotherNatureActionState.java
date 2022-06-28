package it.polimi.ingsw.model.entity.game_state;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;

import java.util.Objects;

/**
 * state pattern state for moving mother nature
 */
public class MoveMotherNatureActionState extends ActionState {

    protected MoveMotherNatureActionState(GameState oldState){
        super(oldState);
        gameState = "MMNS";
    }

    /**
     * Second phase of the Action State, to move mother nature
     * @param playingWizard the player doing the move
     * @param steps the number of steps to move
     */
    public void moveMotherNature(Integer playingWizard, Integer steps) throws GameRuleException {
        moveMotherNatureValidator(playingWizard, steps);
        Game.request(gameId).doMotherNatureSteps(steps);
        Game.request(gameId).getFistIslandGroup().updateTower(Game.request(gameId), activatedCharacter);
        Game.request(gameId).unifyIslands();
        Game.request(gameId).updateGameState(new ChooseCloudActionState(this));
    }

    /**
     * Validator for moveMotherNature method
     * @param playingWizard the player doing the move
     * @param steps the number of steps to move
     * @throws GameRuleException wrong player, phase or too many steps asked
     */
    public void moveMotherNatureValidator(Integer playingWizard, Integer steps) throws GameRuleException {
        if (!Objects.equals(playingWizard, playerOrder.get(currentlyPlaying))) throw new GameRuleException("Wrong player");
        if (!Objects.equals(gameState, "MMNS")) throw new GameRuleException("Wrong game phase");
        if (steps > getMaximumSteps(playingWizard)) throw new GameRuleException("Too many steps");
        if (steps <= 0) throw new GameRuleException("At least one step");
    }

    /**
     * It returns the maximum number of steps mother nature can make in the turn
     * @param playingWizard the player doing the move
     * @return maximum number of steps
     */
    public int getMaximumSteps(Integer playingWizard) {
        int maximumSteps = Game.request(gameId).getWizard(playingWizard).getCardDeck().getCurrentCard().getSteps();
        maximumSteps += activatedCharacter != null && activatedCharacter.getId() == 4 ? 2 : 0;
        return maximumSteps;
    }

}

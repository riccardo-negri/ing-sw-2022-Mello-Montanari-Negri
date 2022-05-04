package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.characters.CharacterFour;
import it.polimi.ingsw.model.enums.Tower;

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
    public void moveMotherNature(Integer playingWizard, Integer steps) throws Exception {
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
     * @throws Exception wrong player, phase or too many steps asked
     */
    public void moveMotherNatureValidator(Integer playingWizard, Integer steps) throws Exception {
        if (playingWizard != playerOrder.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (steps > getMaximumSteps(playingWizard)) throw new Exception("Too many steps");
    }

    /**
     * It returns the maximum number of steps mother nature can make in the turn
     * @param playingWizard the player doing the move
     * @return maximum number of steps
     */
    public int getMaximumSteps(Integer playingWizard) {
        int maximumSteps = Game.request(gameId).getWizard(playingWizard).getCardDeck().getCurrentCard().getSteps();
        maximumSteps += activatedCharacter instanceof CharacterFour ? 2 : 0;
        return maximumSteps;
    }

}

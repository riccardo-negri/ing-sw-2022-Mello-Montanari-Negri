package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFour;
import it.polimi.ingsw.model.enums.Tower;

public class MoveMotherNatureActionState extends ActionState {

    protected MoveMotherNatureActionState(GameState oldState){
        super(oldState);
        gameState = "MMNS";
    }

    /**
     * Second phase of the Action State, to move mother nature
     * @param playingTower the player doing the move
     * @param steps the number of steps to move
     */
    public void moveMotherNature(Tower playingTower, Integer steps) throws Exception {
        moveMotherNatureValidator(playingTower, steps);
        Game.request(gameId).doMotherNatureSteps(steps);
        Game.request(gameId).getFistIslandGroup().updateTower(Game.request(gameId), activatedCharacter);
        Game.request(gameId).unifyIslands();
        Game.request(gameId).updateGameState(new ChooseCloudActionState(this));
    }

    /**
     * Validator for moveMotherNature method
     * @param playingTower the player doing the move
     * @param steps the number of steps to move
     * @throws Exception wrong player, phase or too many steps asked
     */
    public void moveMotherNatureValidator(Tower playingTower, Integer steps) throws Exception {
        if (playingTower != towerOrder.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (steps > getMaximumSteps(playingTower)) throw new Exception("Too many steps");
    }

    /**
     * It returns the maximum number of steps mother nature can make in the turn
     * @param playingTower the player doing the move
     * @return maximum number of steps
     */
    public int getMaximumSteps(Tower playingTower) {
        int maximumSteps = Game.request(gameId).getWizard(playingTower).getCardDeck().getCurrentCard().getSteps();
        maximumSteps += activatedCharacter instanceof CharacterFour ? 2 : 0;
        return maximumSteps;
    }

}

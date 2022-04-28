package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.characters.CharacterFour;

public class MoveMotherNatureActionState extends ActionState {

    protected MoveMotherNatureActionState(GameState oldState){
        super(oldState);
        gameState = "MMNS";
    }

    /**
     * Second phase of the Action State, to move mother nature
     * @param player the player doing the move
     * @param steps the number of steps to move
     */
    void moveMotherNature(Wizard player, Integer steps) throws Exception {
        moveMotherNatureValidator(player, steps);
        game.doMotherNatureSteps(steps);
        game.getFistIslandGroup().updateTower(game, activatedCharacter);
        game.unifyIslands();
        game.updateGameState(new ChooseCloudActionState(this));
    }

    /**
     * Validator for moveMotherNature method
     * @param player the player doing the move
     * @param steps the number of steps to move
     * @throws Exception wrong player, phase or too many steps asked
     */
    public void moveMotherNatureValidator(Wizard player, Integer steps) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (steps > getMaximumSteps(player)) throw new Exception("Too many steps");
    }

    /**
     * It returns the maximum number of steps mother nature can make in the turn
     * @param player the player doing the move
     * @return maximum number of steps
     */
    public int getMaximumSteps(Wizard player) {
        int maximumSteps = player.getCardDeck().getCurrentCard().getSteps();
        maximumSteps += activatedCharacter instanceof CharacterFour ? 2 : 0;
        return maximumSteps;
    }

}

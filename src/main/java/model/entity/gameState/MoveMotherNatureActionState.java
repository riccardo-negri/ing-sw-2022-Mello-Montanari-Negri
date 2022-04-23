package model.entity.gameState;

import model.entity.Wizard;

public class MoveMotherNatureActionState extends ActionState {

    protected MoveMotherNatureActionState(GameState oldState){
        super(oldState);
    }

    /**
     * Second phase of the Action State, to move mother nature
     * @param player the player doing the move
     * @param steps the number of steps to move
     * @throws Exception wrong player, phase or too many steps asked
     */
    void moveMotherNature(Wizard player, Integer steps) throws Exception {
        if (player != order.get(currentlyPlaying)) throw new Exception("Wrong player");
        if (steps > player.getCardDeck().getCurrentCard().getSteps()) throw new Exception("Too many steps");
        game.doMotherNatureSteps(steps);
        game.getFistIslandGroup().updateTower(game, activatedEffectTwo, activatorTwo);
        game.unifyIslands();
        game.updateGameState(new ChooseCloudActionState(this));
    }

}

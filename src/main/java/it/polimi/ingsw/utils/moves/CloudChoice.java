package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ChooseCloudActionState;

public class CloudChoice extends Move{
    private final int cloudPos;

    public CloudChoice(int cloudPos) {
        this.cloudPos = cloudPos;
    }

    @Override
    public void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            //((ChooseCloudActionState) game.getGameState()).;
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            //((ChooseCloudActionState) game.getGameState()).;
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }
}
package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ChooseCloudActionState;

public class CloudChoice extends Move{
    private final int cloudId;

    public CloudChoice(int cloudId) {
        this.cloudId = cloudId;
    }

    @Override
    protected void applyEffect(Game game, Wizard wizard) throws Exception {
        try {
            ((ChooseCloudActionState) game.getGameState()).chooseCloud(wizard.getId(), cloudId);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {
        try {
            ((ChooseCloudActionState) game.getGameState()).chooseCloudValidator(wizard.getId(), cloudId);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    public int getCloudId() {
        return cloudId;
    }
}

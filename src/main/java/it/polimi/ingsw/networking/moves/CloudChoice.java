package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ChooseCloudActionState;

public class CloudChoice extends Move{
    private final int cloudId;

    public CloudChoice(Wizard author, int cloudId) {
        super(author);
        this.cloudId = cloudId;
    }

    @Override
    protected void applyEffect(Game game) throws Exception {
        try {
            ((ChooseCloudActionState) game.getGameState()).chooseCloud(authorId, cloudId);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    @Override
    public void validate(Game game) throws Exception {
        try {
            ((ChooseCloudActionState) game.getGameState()).chooseCloudValidator(authorId, cloudId);
        } catch (ClassCastException e) {
            throw new Exception("This phase doesn't allow this move");
        }
    }

    public int getCloudId() {
        return cloudId;
    }
}

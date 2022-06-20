package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.ChooseCloudActionState;

public class CloudChoice extends Move{
    private final int cloudId;

    /**
     * create a message requesting to pick a cloud
     * @param author the id of the wizard who required the move
     * @param cloudId the id of the picked cloud
     */
    public CloudChoice(Wizard author, int cloudId) {
        super(author);
        this.cloudId = cloudId;
    }

    /**
     * change the game state according to the cloud choice effect
     * @param game the game that is modified by the effect
     * @throws GameRuleException if the move is not valid
     */
    @Override
    protected void applyEffect(Game game) throws GameRuleException {
        try {
            ((ChooseCloudActionState) game.getGameState()).chooseCloud(authorId, cloudId);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * check the validity of the cloud choice without changing the game state
     * @param game the game that gives the information needed for the validation
     * @throws GameRuleException if the move is not valid
     */
    @Override
    public void validate(Game game) throws GameRuleException {
        try {
            ((ChooseCloudActionState) game.getGameState()).chooseCloudValidator(authorId, cloudId);
        } catch (ClassCastException e) {
            throw new GameRuleException("This phase doesn't allow this move");
        }
    }

    /**
     * get cloudId value
     * @return the id of the picked cloud
     */
    public int getCloudId() {
        return cloudId;
    }
}

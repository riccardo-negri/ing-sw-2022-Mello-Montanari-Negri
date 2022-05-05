package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

/**
 * Current state of the game, it has subclasses PlanningState and ActionState for the two main Game States
 */
public abstract class GameState {

    protected String gameState;
    protected List<Integer> playerOrder;
    protected Integer currentlyPlaying;
    protected transient Integer gameId;

    public GameState (List<Integer> playerOrder, Integer gameId) {
        this.currentlyPlaying = 0;
        this.gameId = gameId;
        this.playerOrder = playerOrder;
    }

    public GameState (GameState oldGameState) {
        if (oldGameState instanceof PlanningState) this.currentlyPlaying = 0;
        else this.currentlyPlaying = oldGameState.currentlyPlaying;

        this.gameId = oldGameState.gameId;
        this.playerOrder = oldGameState.playerOrder;
    }

    public void refreshGameId(Game game) { this.gameId = game.getId(); }

    public Integer getCurrentPlayer() { return playerOrder.get(currentlyPlaying); }
}

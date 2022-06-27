package it.polimi.ingsw.model.entity.game_state;

import it.polimi.ingsw.model.entity.Game;

import java.util.List;

/**
 * Current state of the game, it has subclasses PlanningState and ActionState for the two main Game States
 */
public abstract class GameState {

    protected String gameState;
    protected List<Integer> playerOrder;
    protected Integer currentlyPlaying;
    protected transient Integer gameId;

    /**
     * initial costructor, called when the game start
     * @param playerOrder random order of the turn
     * @param gameId id of the game
     */
    protected GameState (List<Integer> playerOrder, Integer gameId) {
        this.currentlyPlaying = 0;
        this.gameId = gameId;
        this.playerOrder = playerOrder;
    }

    /**
     * constructor to be called from existing previous state
     * @param oldGameState previous state
     */
    protected GameState (GameState oldGameState) {
        this.gameId = oldGameState.gameId;
        this.playerOrder = oldGameState.playerOrder;
    }

    /**
     * updates the id of the game (to be done after deserialization)
     * @param game game to take the id from
     */
    public void refreshGameId(Game game) { this.gameId = game.getId(); }

    public Integer getCurrentPlayer() { return playerOrder.get(currentlyPlaying); }

    public List<Integer> getPlayerOrder () {
        return playerOrder;
    }

    public String getGameStateName () {
        return gameState;
    }
}

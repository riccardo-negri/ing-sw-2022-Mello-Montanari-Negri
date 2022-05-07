package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

/**
 * Current state of the game, it has subclasses PlanningState and ActionState for the two main Game States
 */
public abstract class GameState {

    protected String gameState;
    protected List<Tower> towerOrder;
    protected Integer currentlyPlaying;
    protected Integer gameId;

    public GameState (List<Tower> towerOrder, Integer gameId) {
        this.currentlyPlaying = 0;
        this.gameId = gameId;
        this.towerOrder = towerOrder;
    }

    public GameState (GameState oldGameState) {
        if (oldGameState instanceof PlanningState) this.currentlyPlaying = 0;
        else this.currentlyPlaying = oldGameState.currentlyPlaying;

        this.gameId = oldGameState.gameId;
        this.towerOrder = oldGameState.towerOrder;
    }

    public Tower getCurrentPlayer() { return towerOrder.get(currentlyPlaying); }
}

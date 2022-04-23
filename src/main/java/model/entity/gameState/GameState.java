package model.entity.gameState;

import model.entity.Game;
import model.entity.Wizard;

import java.util.List;

/**
 * Current state of the game, it has subclasses PlanningState and ActionState for the two main Game States
 */
public abstract class GameState {

    protected List<Wizard> order;
    protected Integer currentlyPlaying;
    protected Game game;

    public GameState (List<Wizard> order, Game game) {
        this.currentlyPlaying = 0;
        this.game = game;
        this.order = order;
    }

    public GameState (GameState oldGameState) {
        if (oldGameState instanceof PlanningState) this.currentlyPlaying = 0;
        else this.currentlyPlaying = oldGameState.currentlyPlaying;

        this.game = oldGameState.game;
        this.order = oldGameState.order;
    }
}

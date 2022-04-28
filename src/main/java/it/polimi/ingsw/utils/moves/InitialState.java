package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.GameState;

public class InitialState extends Move {
    private final GameState state;
    public InitialState() {this.state = null;}

    @Override
    public void applyEffect(Game game, Wizard wizard) throws Exception {
        game.updateGameState(state);
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {}
}

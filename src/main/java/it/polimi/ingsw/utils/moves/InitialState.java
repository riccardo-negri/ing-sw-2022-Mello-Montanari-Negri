package it.polimi.ingsw.utils.moves;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.gameState.GameState;

public class InitialState extends Move {
    private final String state;
    public InitialState(String state) {this.state = state;}

    @Override
    public void applyEffect(Game game, Wizard wizard) throws Exception {
        Game.deserializeGame(state); //TODO: replace state instead of create new game
    }

    @Override
    public void validate(Game game, Wizard wizard) throws Exception {}
}

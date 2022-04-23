package model.entity.gameState;


public abstract class ActionState extends GameState {

    public ActionState(GameState oldState) {
        super(oldState);
    }

}

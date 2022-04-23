package model.entity.gameState;


import model.entity.Wizard;

public abstract class ActionState extends GameState {

    protected boolean activatedEffectTwo;
    protected Wizard activatorTwo;

    public ActionState(GameState oldState) {
        super(oldState);
        if(oldState instanceof ChooseCloudActionState || oldState instanceof PlanningState) {
            activatedEffectTwo = false;
            activatorTwo = null;
        } else {
            ActionState state = (ActionState) oldState;
            activatedEffectTwo = state.activatedEffectTwo;
            activatorTwo = state.activatorTwo;
        }
    }

    public void activateEffectTwo(Wizard activator) {
        activatorTwo = activator;
        activatedEffectTwo = true;
    }

}

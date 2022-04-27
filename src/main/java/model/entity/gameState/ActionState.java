package model.entity.gameState;

import model.entity.characters.Character;

public abstract class ActionState extends GameState {

    protected Character activatedCharacter;

    public ActionState(GameState oldState) {
        super(oldState);
        if(oldState instanceof ChooseCloudActionState || oldState instanceof PlanningState) {
            activatedCharacter = null;
        } else {
            ActionState state = (ActionState) oldState;
            activatedCharacter = ((ActionState) oldState).activatedCharacter;
        }
    }

    public void activateEffect(Character activatedCharacter) {
        this.activatedCharacter = activatedCharacter;
    }

}

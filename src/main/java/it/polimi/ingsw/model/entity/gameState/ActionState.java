package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.characters.Character;

public abstract class ActionState extends GameState {
    protected Character activatedCharacter;

    /**
     * Constructor to call when a new turn starts, it clears previous activator
     * @param oldState previous state to update from
     */
    protected ActionState(GameState oldState) {
        super(oldState);

        if (oldState instanceof PlanningState) this.currentlyPlaying = 0;
        else this.currentlyPlaying = oldState.currentlyPlaying;

        if(oldState instanceof ChooseCloudActionState || oldState instanceof PlanningState) {
            activatedCharacter = null;
        } else {
            activatedCharacter = ((ActionState) oldState).activatedCharacter;
        }
    }

    /**
     * sets the activator of the effect
     * @param activatedCharacter character to activate
     */
    public void activateEffect(Character activatedCharacter) {
        this.activatedCharacter = activatedCharacter;
    }

    public Character getActivatedCharacter () {
        return activatedCharacter;
    }
}

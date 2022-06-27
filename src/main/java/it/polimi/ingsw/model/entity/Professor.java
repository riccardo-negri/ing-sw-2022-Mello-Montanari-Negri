package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.Objects;

/**
 * contains the wizard owning the professor
 */
public class Professor {

    private transient Integer gameId;
    private final StudentColor color;
    private Integer master;

    public Professor(Integer gameId, StudentColor color) {
        this.gameId = gameId;
        this.color = color;
        this.master = null;
    }

    /**
     * Reset the game id after the deserialization process
     * @param game the new game object
     */
    public void refreshGameId(Game game) { this.gameId = game.getId(); }

    /**
     * used to calculate the new owner of the professor
     * @param contestant the wizard who may become the new owner, the one who added some students
     */
    public void refreshMaster(Integer contestant) {
        Game game = Game.request(gameId);
        ActionState state = (ActionState) game.getGameState();
        if (master == null) master = contestant;
        else if ((game.getWizard(contestant).getDiningStudents(color) > game.getWizard(master).getDiningStudents(color)) ||
                (state.getActivatedCharacter() != null && state.getActivatedCharacter().getId() == 2 &&
                        Objects.equals(game.getWizard(contestant).getDiningStudents(color), game.getWizard(master).getDiningStudents(color))))
            master = contestant;
    }

    /**
     * get the wizard owning the professor
     * @return the wizard owning the professor
     */
    public Wizard getMaster() {
        return Game.request(gameId).getWizard(master);
    }

}

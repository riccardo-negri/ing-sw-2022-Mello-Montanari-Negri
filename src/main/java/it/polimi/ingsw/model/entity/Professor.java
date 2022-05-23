package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.characters.CharacterTwo;
import it.polimi.ingsw.model.enums.Tower;

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

    public Wizard getMaster(Character activatedCharacter) { return Game.request(gameId).getWizard(master); }

}

package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.characters.CharacterTwo;

/**
 * contains the wizard owning the professor
 */
public class Professor {

    private final StudentColor color;
    private Wizard master;

    public Professor(StudentColor color) {
        this.color = color;
        this.master = null;
    }

    /**
     * used to calculate the new owner of the professor
     * @param contestant the wizard who may become the new owner, the one who added some students
     */
    public void refreshMaster(Wizard contestant) {
        if (contestant.getDiningStudents(color) > master.getDiningStudents(color)) master = contestant;
    }

    public Wizard getMaster(Character activatedCharacter) {
        if (activatedCharacter instanceof CharacterTwo && activatedCharacter.getActivator().getDiningStudents(color) >= master.getDiningStudents(color))
            return activatedCharacter.getActivator();
        else return master;
    }

}

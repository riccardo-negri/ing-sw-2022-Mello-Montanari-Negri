package model.entity;

import model.enums.StudentColor;

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

    public Wizard getMaster() { return master; }
}

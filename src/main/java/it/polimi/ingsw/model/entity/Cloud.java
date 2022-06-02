package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

public class Cloud {

    private final Integer cloudId;
    private transient Bag bag;
    private final PlayerNumber playerNumber;
    private List<StudentColor> studentColorList;
    private boolean taken;

    /**
     * creates an empty cloud
     * @param bag the bag to take students from
     * @param playerNumber the number of player to fill at the beginning of the turn
     */
    public Cloud(Integer cloudId, Bag bag, PlayerNumber playerNumber) {
        this.cloudId = cloudId;
        this.bag = bag;
        this.playerNumber = playerNumber;
    }

    public List<StudentColor> getCloudContent() {
        return studentColorList;
    }

    protected void setBag(Bag bag) { this.bag = bag; }

    /**
     * to take the student from the cloud at the end of the Action State
     * @return the list of taken students present on the island
     * @throws GameRuleException if the cloud is already taken
     */
    public List<StudentColor> takeStudents() throws GameRuleException {
        if (taken) throw new GameRuleException("Cloud already taken");
        taken = true;
        List<StudentColor> out = studentColorList;
        studentColorList = new ArrayList<>();
        return out;
    }

    /**
     * to fill the cloud with new students at the beginning of the planning phase
     */
    public void fillCloud() {
        studentColorList = bag.requestStudents(playerNumber.getCloudSize());
        taken = false;
    }

    public boolean isTaken() { return taken; }

    public Integer getId() { return cloudId; }
}

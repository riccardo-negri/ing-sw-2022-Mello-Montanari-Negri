package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.entity.characters.CharacterFive;

import java.util.List;

/**
 * contains the students on the map
 */
public class Island {

    private final Integer islandId;
    private boolean stopCard;
    private CharacterFive characterFive;
    private final List<StudentColor> studentColorList;

    /**
     * constructor for island
     * @param studentColorList list containing the initial students (0 or 1) on the island
     * @param islandId the id of the island (0 to 11)
     */
    public Island(List<StudentColor> studentColorList, Integer islandId) {
        this.islandId = islandId;
        this.studentColorList = studentColorList;
        stopCard = false;
    }

    /**
     * Add 1 student to the island
     * @param studentColor color of the student to add
     */
    public void putIslandStudent (StudentColor studentColor) { studentColorList.add(studentColor); }

    public List<StudentColor> getStudentColorList() { return studentColorList; }

    /**
     * Add a stop card on the island (effect by character 5)
     * @param characterFive character five object
     */
    public void addStopCard(CharacterFive characterFive) {
        this.characterFive = characterFive;
        stopCard = true;
        characterFive.removeOneCard();
    }

    /**
     * Remove the stop card when mother nature passes on the island
     */
    public void removeStopCard() {
        stopCard = false;
        characterFive.addOneCard();
    }

    public boolean hasStopCard() { return stopCard; }

    public Integer getId() { return islandId; }
}

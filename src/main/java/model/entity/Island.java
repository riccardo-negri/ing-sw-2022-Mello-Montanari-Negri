package model.entity;

import model.entity.characters.CharacterFive;
import model.enums.StudentColor;

import java.util.List;

/**
 * contains the students on the map
 */
public class Island {

    private boolean stopCard;
    private CharacterFive characterFive;
    private final List<StudentColor> studentColorList;

    public Island(List<StudentColor> studentColorList) {
        this.studentColorList = studentColorList;
        stopCard = false;
    }

    public void putIslandStudent (StudentColor studentColor) { studentColorList.add(studentColor); }

    public List<StudentColor> getStudentColorList() { return studentColorList; }

    public void addStopCard(CharacterFive characterFive) {
        this.characterFive = characterFive;
        stopCard = true;
        characterFive.removeOneCard();
    }

    public void removeStopCard() {
        stopCard = false;
        characterFive.addOneCard();
    }

    public boolean isStopCard() { return stopCard; }
}

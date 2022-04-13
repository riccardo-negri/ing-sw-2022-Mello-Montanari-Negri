package model.entity;

import model.enums.StudentColor;

import java.util.List;

/**
 * contains the students on the map
 */
public class Island {
    private final List<StudentColor> studentColorList;

    public Island(List<StudentColor> studentColorList) {
        this.studentColorList = studentColorList;
    }

    public void putIslandStudent (StudentColor studentColor) { studentColorList.add(studentColor); }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}

package it.polimi.ingsw.model.entity.bag;


import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

/**
 * the bag to randomly take students
 */
public abstract class Bag {

    public abstract List<StudentColor> requestStudents(int num) throws Exception;

    public abstract List<StudentColor> takeRecentlySelected();

    public abstract void putRecentlySelected(List<StudentColor> studentColorList);
}

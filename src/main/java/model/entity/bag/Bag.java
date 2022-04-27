package model.entity.bag;


import model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * the bag to randomly take students
 */
public abstract class Bag {

    public abstract List<StudentColor> requestStudents(int num) throws Exception;

    public abstract List<StudentColor> takeRecentlySelected();

    public abstract void putRecentlySelected(List<StudentColor> studentColorList);
}

package it.polimi.ingsw.model.entity;


import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * the bag to randomly take students
 */
public class Bag {

    private transient Random randomGenerator;
    private List<StudentColor> studentListIn;
    private List<StudentColor> studentListOut;
    private final Integer[] colorNumber;
    private Integer total;

    public Bag (Random randomGenerator) {
        colorNumber = new Integer[5];
        for (int i=0; i<5; i++)
            colorNumber[i] = 26;
        total = 130;
        this.randomGenerator = randomGenerator;
        studentListOut = new ArrayList<>();
        studentListIn = new ArrayList<>();
    }

    /**
     * To randomly extract num students from bag
     * @param num Number of students to extract
     * @return List of StudentColor containing extracted students
     * @throws Exception not enough students in the bag
     */
    public List<StudentColor> requestStudents(int num) throws Exception {
        if (total < num) throw new Exception("Not enough students in bag");
        List<StudentColor> students = new ArrayList<>();
        if (studentListIn.isEmpty()) {
            if (randomGenerator==null) randomGenerator = new Random();
            for (int i = 0; i < num; i++) {
                int value = randomGenerator.nextInt(total--);
                for (int count = 0; count < 5; count++)
                    if ((value = value - colorNumber[count]) < 0) {
                        students.add(StudentColor.fromNumber(count));
                        colorNumber[count]--;
                        studentListOut.add(StudentColor.fromNumber(count));
                        break;
                    }
            }
        } else {
            if (num > studentListIn.size()) throw new Exception("Too many students selected");
            total -= num;
            students = studentListIn.subList(0,num);
            studentListIn = studentListIn.subList(num,studentListIn.size());
            students.forEach(x -> colorNumber[x.getValue()]--);
        }
        return students;
    }

    public List<StudentColor> takeRecentlySelected() {
        List<StudentColor> out = studentListOut;
        studentListOut = new ArrayList<>();
        return out;
    }

    public void putRecentlySelected(List<StudentColor> studentColorList) {
        studentListIn.addAll(studentColorList);
    }
}

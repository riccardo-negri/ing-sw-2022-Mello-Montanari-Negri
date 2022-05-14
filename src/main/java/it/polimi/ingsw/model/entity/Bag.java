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
    private boolean empty;

    public Bag (Random randomGenerator) {
        colorNumber = new Integer[5];
        for (int i=0; i<5; i++)
            colorNumber[i] = 26;
        total = 130;
        empty = false;
        this.randomGenerator = randomGenerator;
        studentListOut = new ArrayList<>();
        studentListIn = new ArrayList<>();
    }

    /**
     * To randomly extract num students from bag
     * @param num Number of students to extract
     * @return List of StudentColor containing extracted students
     */
    public List<StudentColor> requestStudents(int num) {
        if (total < num) {
            num = total;
            empty = true;
        }
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
            total -= num;
            students = studentListIn.subList(0,num);
            studentListIn = studentListIn.subList(num,studentListIn.size());
            students.forEach(x -> colorNumber[x.getValue()]--);
        }
        return students;
    }

    /**
     * Method to initialize the island students (needs to be done separately to avoid having more
     * than two students in the same color (imperfect probability but good enough for the scope)
     * @return null if the method is called after the beginning of the game, the list of 10 students otherwise
     */
    public List<StudentColor> requestStartingBoard() {
        if (total != 130) return null;
        List<StudentColor> students = new ArrayList<>();
        if (studentListIn.isEmpty()) {
            if (randomGenerator==null) randomGenerator = new Random();
            for (int i = 0; i < 10; i++) {
                int value = 0;
                boolean flag = true;
                while (flag) {
                    flag = false;
                    value = randomGenerator.nextInt(5);
                    for (StudentColor s : StudentColor.values())
                        if (students.stream().filter(x -> x==s).count()==2 && value == s.getValue()) flag = true;
                }
                students.add(StudentColor.fromNumber(value));
                colorNumber[value]--;
                studentListOut.add(StudentColor.fromNumber(value));
            }
        } else
            students = requestStudents(10);
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

    public boolean isEmpty() {
        return empty;
    }
}

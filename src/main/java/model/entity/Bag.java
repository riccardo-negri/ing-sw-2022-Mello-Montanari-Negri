package model.entity;


import model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * the bag to randomly take students
 */
public class Bag {

    private final Integer[] colorNumber;
    private Integer total;
    private final Random randomGenerator;

    public Bag(Random randomGenerator) {
        colorNumber = new Integer[5];
        for (int i=0; i<5; i++)
            colorNumber[i] = 26;
        total = 130;
        this.randomGenerator = randomGenerator;
    }

    /**
     * To randomly extract num students from bag
     * @param num Number of students to extract
     * @return List of StudentColor containing extracted students
     * @throws Exception not enough students in the bag
     */
    public List<StudentColor> requestStudents(int num) throws Exception{
        if(total < num) throw new Exception("Not enough items in bag");
        List<StudentColor> students = new ArrayList<StudentColor>();
        for (int i=0; i<num; i++) {
            int value = randomGenerator.nextInt(total--);
            for (int count = 0; count < 5; count++)
                if((value = value - colorNumber[count]) < 0) {
                    students.add(StudentColor.fromNumber(count));
                    colorNumber[count]--;
                    break;
                }
        }
        return students;
    }
}

package model.entity.bag;

import model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerBag extends Bag {

    private final Random randomGenerator;
    private StudentColor chooseClient;
    private List<StudentColor> recentlySelected;
    protected final Integer[] colorNumber;
    protected Integer total;

    public ServerBag(Random randomGenerator) {
        colorNumber = new Integer[5];
        for (int i=0; i<5; i++)
            colorNumber[i] = 26;
        total = 130;
        this.randomGenerator = randomGenerator;
        recentlySelected = new ArrayList<StudentColor>();
    }

    /**
     * To randomly extract num students from bag
     * @param num Number of students to extract
     * @return List of StudentColor containing extracted students
     * @throws Exception not enough students in the bag
     */
    public List<StudentColor> requestStudents(int num) throws Exception{
        if (total < num) throw new Exception("Not enough items in bag");
        List<StudentColor> students = new ArrayList<StudentColor>();
        for (int i = 0; i < num; i++) {
            int value = randomGenerator.nextInt(total--);
            for (int count = 0; count < 5; count++)
                if ((value = value - colorNumber[count]) < 0) {
                    students.add(StudentColor.fromNumber(count));
                    colorNumber[count]--;
                    recentlySelected.add(StudentColor.fromNumber(count));
                    break;
                }
        }
        return students;
    }

    public List<StudentColor> takeRecentlySelected() {
        List<StudentColor> out = recentlySelected;
        recentlySelected = new ArrayList<>();
        return out;
    }

    public void putRecentlySelected(List<StudentColor> studentColorList) {
        // This method is not supposed to be called in server
    }
}

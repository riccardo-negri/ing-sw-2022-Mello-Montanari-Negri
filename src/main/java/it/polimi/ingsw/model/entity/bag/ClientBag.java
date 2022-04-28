package it.polimi.ingsw.model.entity.bag;

import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

public class ClientBag extends Bag{

    private List<StudentColor> recentlySelected;

    public ClientBag() {
        super();
        recentlySelected = new ArrayList<>();
    }

    /**
     * To randomly extract num students from bag
     * @param num Number of students to extract
     * @return List of StudentColor containing extracted students
     * @throws Exception not enough students in the bag
     */
    public List<StudentColor> requestStudents(int num) throws Exception {
        if (num > recentlySelected.size()) throw new Exception("Too many students selected");
        List<StudentColor> out = recentlySelected.subList(0,num);
        recentlySelected = recentlySelected.subList(num,recentlySelected.size());
        return out;
    }

    public List<StudentColor> takeRecentlySelected() {
        // This method is not supposed to be called in the client
        return null;
    }

    public void putRecentlySelected(List<StudentColor> studentColorList) {
        recentlySelected.addAll(studentColorList);
    }
}

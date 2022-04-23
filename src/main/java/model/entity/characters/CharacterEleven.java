package model.entity.characters;

import model.entity.Bag;
import model.entity.Game;
import model.entity.Island;
import model.entity.Wizard;
import model.enums.StudentColor;

import java.util.List;

public class CharacterEleven extends Character{

    private final Bag bag;
    private List<StudentColor> studentColorList;

    public CharacterEleven(Bag bag) {
        super(2);
        this.bag = bag;
        try {
            studentColorList = bag.requestStudents(4);
        } catch (Exception e) {}
    }

    public List<StudentColor> getStudentColorList() { return studentColorList; }

    public void useEffect(Wizard player, Game game, StudentColor request) throws Exception{
        if (!studentColorList.contains(request)) throw new Exception("Student color not present on the card");
        useCard(player);
        studentColorList.remove(request);
        player.putDiningStudent(request);
        studentColorList.addAll(bag.requestStudents(1));
    }
}


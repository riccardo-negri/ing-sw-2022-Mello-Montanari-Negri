package model.entity.characters;

import model.entity.Bag;
import model.entity.Game;
import model.entity.Island;
import model.entity.Wizard;
import model.enums.StudentColor;

import java.util.List;

public class CharacterOne extends Character{

    private final Bag bag;
    private List<StudentColor> studentColorList;

    public CharacterOne(Bag bag) {
        super(1);
        this.bag = bag;
        try {
            studentColorList = bag.requestStudents(4);
        } catch (Exception e) {}
    }

    public List<StudentColor> getStudentColorList() { return studentColorList; }

    public void useEffect(Wizard player, Game game, StudentColor request, Island island) throws Exception{
        if (!studentColorList.contains(request)) throw new Exception("Student color not present on the card");
        useCard(player);
        studentColorList.remove(request);
        island.putIslandStudent(request);
        studentColorList.addAll(bag.requestStudents(1));

    }
}

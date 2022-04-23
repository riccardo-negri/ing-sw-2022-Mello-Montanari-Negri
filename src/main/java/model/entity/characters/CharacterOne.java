package model.entity.characters;

import model.entity.Bag;
import model.entity.Game;
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

    @Override
    public void useEffect(Wizard player, Game game, Object request) throws Exception{
        if (!(request instanceof StudentColor)) throw new Exception("Bad request, not a Student");
        if (!studentColorList.contains((StudentColor) request)) throw new Exception("Student color not present on the card");
        useCard(player);
        studentColorList.remove((StudentColor) request);
        studentColorList.addAll(bag.requestStudents(1));

    }
}

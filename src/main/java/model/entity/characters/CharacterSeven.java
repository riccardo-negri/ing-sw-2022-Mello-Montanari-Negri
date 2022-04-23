package model.entity.characters;

import model.entity.Bag;
import model.entity.Game;
import model.entity.Wizard;
import model.enums.StudentColor;

import java.util.List;

public class CharacterSeven extends Character{

    private List<StudentColor> studentColorList;

    public CharacterSeven (Bag bag) {
        super(1);
        try { studentColorList = bag.requestStudents(6);
        } catch (Exception e) { }

    }

    public void useEffect(Wizard player, Game game, List<StudentColor> take, List<StudentColor> give) throws Exception {
        if (!studentColorList.containsAll(take)) throw new Exception("Students not available on the card");
        if (!player.getEntranceStudents().containsAll(give)) throw new Exception("Students not available in the entrance");
        if(give.size()>3 || give.size() != take.size()) throw new Exception("Inexact number of students");
        useCard(player);
        studentColorList.removeAll(take);
        studentColorList.addAll(give);
        player.getEntranceStudents().removeAll(give);
        player.getEntranceStudents().addAll(take);
    }
}

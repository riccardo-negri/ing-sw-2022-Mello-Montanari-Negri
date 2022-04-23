package model.entity.characters;

import model.entity.Game;
import model.entity.Wizard;
import model.enums.StudentColor;

import java.util.List;

public class CharacterTen extends Character {

    public CharacterTen () {
        super(1);
    }

    public void useEffect(Wizard player, Game game, List<StudentColor> take, List<StudentColor> give) throws Exception {
        if (!player.getEntranceStudents().containsAll(take)) throw new Exception("Students not present in entrance");
        for (StudentColor color : StudentColor.values())
            if (give.stream().filter(x -> x == color).count() > player.getDiningStudents(color)) throw new Exception("Students not present in dining room");
        if (take.size() > 2 || take.size() != give.size()) throw new Exception("Inexact number of students");
        useCard(player);
        for (StudentColor color : give) player.takeDiningStudent(color);
        for (StudentColor color : take) player.putDiningStudent(color);
        player.getEntranceStudents().removeAll(take);
        player.getEntranceStudents().addAll(give);
    }
}

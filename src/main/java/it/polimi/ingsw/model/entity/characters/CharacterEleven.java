package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.bag.Bag;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

public class CharacterEleven extends Character{

    private final Bag bag;
    private List<StudentColor> studentColorList;

    public CharacterEleven(Bag bag) {
        super(2);
        this.bag = bag;
        try { studentColorList = bag.requestStudents(4);
        } catch (Exception e) {}
    }

    /**
     * one student from the card can be taken and put in the dining room
     * @param player the player playing the card
     * @param game the current game
     * @param studentColor the color of the student to take
     */
    public void useEffect(Wizard player, Game game, StudentColor studentColor) throws Exception{
        characterElevenValidator(player, game, studentColor);
        useCard(player);
        studentColorList.remove(studentColor);
        player.putDiningStudent(studentColor);
        studentColorList.addAll(bag.requestStudents(1));
    }

    /**
     * one student from the card can be taken and put in the dining room
     * @param player the player playing the card
     * @param game the current game
     * @param studentColor the color of the student to take
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * or if the student is not present on the card
     */
    public void characterElevenValidator(Wizard player, Game game, StudentColor studentColor) throws Exception{
        characterValidator(player, game);
        if (!studentColorList.contains(studentColor)) throw new Exception("Student color not present on the card");
    }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}


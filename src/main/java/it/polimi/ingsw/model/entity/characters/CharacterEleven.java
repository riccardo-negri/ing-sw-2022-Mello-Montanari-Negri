package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

public class CharacterEleven extends Character{

    private final transient Bag bag;
    private final List<StudentColor> studentColorList;

    public CharacterEleven(Integer gameId, Integer characterId, Bag bag) {
        super(gameId, characterId, 2);
        this.bag = bag;
        studentColorList = bag.requestStudents(4);
    }

    /**
     * one student from the card can be taken and put in the dining room
     * @param playingWizard the player playing the card
     * @param studentColor the color of the student to take
     */
    public void useEffect(Integer playingWizard, StudentColor studentColor) throws Exception{
        characterElevenValidator(playingWizard, studentColor);
        useCard(playingWizard);
        studentColorList.remove(studentColor);
        Game.request(gameId).getWizard(playingWizard).putDiningStudent(studentColor);
        studentColorList.addAll(bag.requestStudents(1));
    }

    /**
     * one student from the card can be taken and put in the dining room
     * @param playingWizard the player playing the card
     * @param studentColor the color of the student to take
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * or if the student is not present on the card
     */
    public void characterElevenValidator(Integer playingWizard, StudentColor studentColor) throws Exception{
        characterValidator(playingWizard);
        if (!studentColorList.contains(studentColor)) throw new Exception("Student color not present on the card");
    }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}


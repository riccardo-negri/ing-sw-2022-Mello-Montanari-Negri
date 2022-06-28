package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

/**
 * character 11 class with logic
 */
public class CharacterEleven extends Character{

    private transient Bag bag;
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
    public void useEffect(Integer playingWizard, StudentColor studentColor) throws GameRuleException{
        characterElevenValidator(playingWizard, studentColor);
        useCard(playingWizard);
        studentColorList.remove(studentColor);
        Game.request(gameId).getWizard(playingWizard).putDiningStudent(studentColor);
        studentColorList.addAll(bag.requestStudents(1));
        Game.request(gameId).getProfessor(studentColor).refreshMaster(playingWizard);
    }

    /**
     * one student from the card can be taken and put in the dining room
     * @param playingWizard the player playing the card
     * @param studentColor the color of the student to take
     * @throws GameRuleException if it is not the player turn, he does not have enough money to activate the card,
     * or if the student is not present on the card
     */
    public void characterElevenValidator(Integer playingWizard, StudentColor studentColor) throws GameRuleException {
        characterValidator(playingWizard);
        if (!studentColorList.contains(studentColor)) throw new GameRuleException("Student color not present on the card");
        Game.request(gameId).getWizard(playingWizard).checkDiningStudentNumber(studentColor);
    }

    /**
     * sets the new bag (to do after deserialization)
     * @param bag the new bag
     */
    public void refreshBag (Bag bag) { this.bag = bag; }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}


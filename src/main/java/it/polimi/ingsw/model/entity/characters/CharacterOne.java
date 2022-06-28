package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

/**
 * character 1 class with logic
 */
public class CharacterOne extends Character{

    private transient Bag bag;
    private final List<StudentColor> studentColorList;

    public CharacterOne(Integer gameId, Integer characterId, Bag bag) {
        super(gameId, characterId,1);
        this.bag = bag;
        studentColorList = bag.requestStudents(4);
    }

    /**
     * take one student and put it on a chosen island, then refill the card from the bag
     * @param playingWizard the player playing the card
     * @param studentColor the color of the student to take
     * @param islandId the island to put the student on
     */
    public void useEffect(Integer playingWizard, StudentColor studentColor, Integer islandId) throws GameRuleException {
        characterOneValidator(playingWizard, studentColor, islandId);
        useCard(playingWizard);
        studentColorList.remove(studentColor);
        Game.request(gameId).getIsland(islandId).putIslandStudent(studentColor);
        studentColorList.addAll(bag.requestStudents(1));
    }

    /**
     * Validator for character one useEffect method
     * @param playingWizard the player playing the card
     * @param studentColor the color of the student to take
     * @param islandId the island to put the student on
     * @throws GameRuleException if it is not the player turn, he does not have enough money to activate the card,
     * the card does not contain the student asked or the island does not exist
     */
    public void characterOneValidator (Integer playingWizard, StudentColor studentColor, Integer islandId) throws GameRuleException{
        characterValidator(playingWizard);
        if (!studentColorList.contains(studentColor)) throw new GameRuleException("Student color not present on the card");
        if (islandId<0 || islandId>=12) throw new GameRuleException("Selected island doesn't exist");
    }

    /**
     * sets the new bag (to do after deserialization)
     * @param bag the new bag
     */
    public void refreshBag (Bag bag) { this.bag = bag; }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}

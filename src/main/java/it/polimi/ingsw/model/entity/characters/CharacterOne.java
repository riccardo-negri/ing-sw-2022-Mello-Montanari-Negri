package it.polimi.ingsw.model.entity.characters;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Island;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.bag.Bag;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

public class CharacterOne extends Character{

    private final transient Bag bag;
    private List<StudentColor> studentColorList;

    public CharacterOne(Integer gameId, Integer characterId, Bag bag) {
        super(gameId, characterId,1);
        this.bag = bag;
        try { studentColorList = bag.requestStudents(4);
        } catch (Exception e) {}
    }

    /**
     * take one student and put it on a chosen island, then refill the island from the bag
     * @param playingTower the player playing the card
     * @param studentColor the color of the student to take
     * @param islandId the island to put the student on
     */
    public void useEffect(Tower playingTower, StudentColor studentColor, Integer islandId) throws Exception{
        characterOneValidator(playingTower, studentColor, islandId);
        useCard(playingTower);
        studentColorList.remove(studentColor);
        Game.request(gameId).getIsland(islandId).putIslandStudent(studentColor);
        studentColorList.addAll(bag.requestStudents(1));
    }

    /**
     * Validator for character one useEffect method
     * @param playingTower the player playing the card
     * @param studentColor the color of the student to take
     * @param islandId the island to put the student on
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the card does not contain the student asked or the island does not exist
     */
    public void characterOneValidator (Tower playingTower, StudentColor studentColor, Integer islandId) throws Exception{
        characterValidator(playingTower);
        if (!studentColorList.contains(studentColor)) throw new Exception("Student color not present on the card");
        if (islandId<0 || islandId>=12) throw new Exception("Selected island doesn't exist");
    }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}

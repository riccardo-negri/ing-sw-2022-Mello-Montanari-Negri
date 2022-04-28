package it.polimi.ingsw.model.entity.characters;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Island;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.entity.bag.Bag;
import it.polimi.ingsw.model.enums.StudentColor;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;

public class CharacterOne extends Character{

    private final transient Bag bag;
    private List<StudentColor> studentColorList;

    public CharacterOne(Integer characterId, Bag bag) {
        super(characterId,1);
        this.bag = bag;
        try { studentColorList = bag.requestStudents(4);
        } catch (Exception e) {}
    }

    public CharacterOne(JsonObject jsonObject) {
        super(1, 1);
        this.bag = null;    //TODO da sistemare questione bag
        studentColorList = new ArrayList<>();
        jsonObject.get("studentColorList").getAsJsonArray().iterator()
                .forEachRemaining(x -> studentColorList.add(StudentColor.valueOf(x.toString().substring(1,-1))));
        System.out.println(studentColorList);
    }

    /**
     * take one student and put it on a chosen island, then refill the island from the bag
     * @param player the player playing the card
     * @param game the current game
     * @param studentColor the color of the student to take
     * @param island the island to put the student on
     */
    public void useEffect(Wizard player, Game game, StudentColor studentColor, Island island) throws Exception{
        characterOneValidator(player, game, studentColor, island);
        useCard(player);
        studentColorList.remove(studentColor);
        island.putIslandStudent(studentColor);
        studentColorList.addAll(bag.requestStudents(1));
    }

    /**
     * Validator for character one useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @param studentColor the color of the student to take
     * @param island the island to put the student on
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the card does not contain the student asked or the island does not exist
     */
    public void characterOneValidator (Wizard player, Game game, StudentColor studentColor, Island island) throws Exception{
        characterValidator(player, game);
        if (!studentColorList.contains(studentColor)) throw new Exception("Student color not present on the card");
        if (game.getIslandGroupList().stream().flatMap(x -> x.getIslandList().stream()).noneMatch(x -> x == island))
            throw new Exception("Island does not exist");
    }

    public List<StudentColor> getStudentColorList() { return studentColorList; }
}

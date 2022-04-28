package model.entity.characters;

import model.entity.bag.Bag;
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

    /**
     * exchange up to three student on this card with three students in the entrance
     * @param player the player playing the card
     * @param game the current game
     * @param take students to take from the card
     * @param give students to take from the entrance
     */
    public void useEffect(Wizard player, Game game, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterSevenValidator(player, game, take, give);
        useCard(player);
        studentColorList.removeAll(take);
        studentColorList.addAll(give);
        player.getEntranceStudents().removeAll(give);
        player.getEntranceStudents().addAll(take);
    }

    /**
     * Validator for character seven useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @param take students to take from the card
     * @param give students to take from the entrance
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterSevenValidator(Wizard player, Game game, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterValidator(player, game);
        if (!studentColorList.containsAll(take)) throw new Exception("Students not available on the card");
        if (!player.getEntranceStudents().containsAll(give)) throw new Exception("Students not available in the entrance");
        if(give.size()>3 || give.size() != take.size()) throw new Exception("Inexact number of students");
    }
}

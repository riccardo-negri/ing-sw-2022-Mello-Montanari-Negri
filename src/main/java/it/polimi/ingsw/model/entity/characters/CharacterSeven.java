package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

public class CharacterSeven extends Character{

    private List<StudentColor> studentColorList;

    public CharacterSeven (Integer gameId, Integer characterId, Bag bag) {
        super(gameId, characterId, 1);
        try { studentColorList = bag.requestStudents(6);
        } catch (Exception e) { }

    }

    /**
     * exchange up to three student on this card with three students in the entrance
     * @param playingTower the player playing the card
     * @param take students to take from the card
     * @param give students to take from the entrance
     */
    public void useEffect(Tower playingTower, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterSevenValidator(playingTower, take, give);
        useCard(playingTower);
        studentColorList.removeAll(take);
        studentColorList.addAll(give);
        Game.request(gameId).getWizard(playingTower).getEntranceStudents().removeAll(give);
        Game.request(gameId).getWizard(playingTower).getEntranceStudents().addAll(take);
    }

    /**
     * Validator for character seven useEffect method
     * @param playingTower the player playing the card
     * @param take students to take from the card
     * @param give students to take from the entrance
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterSevenValidator(Tower playingTower, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterValidator(playingTower);
        if (!studentColorList.containsAll(take)) throw new Exception("Students not available on the card");
        if (!Game.request(gameId).getWizard(playingTower).getEntranceStudents().containsAll(give))
            throw new Exception("Students not available in the entrance");
        if(give.size()>3 || give.size() != take.size()) throw new Exception("Inexact number of students");
    }
}

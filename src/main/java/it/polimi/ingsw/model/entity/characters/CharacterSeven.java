package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Bag;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

/**
 * character 7 class with logic
 */
public class CharacterSeven extends Character{

    private final List<StudentColor> studentColorList;

    public CharacterSeven (Integer gameId, Integer characterId, Bag bag) {
        super(gameId, characterId, 1);
        studentColorList = bag.requestStudents(6);

    }

    /**
     * exchange up to three student on this card with three students in the entrance
     * @param playingWizard the player playing the card
     * @param take students to take from the card
     * @param give students to take from the entrance
     */
    public void useEffect(Integer playingWizard, List<StudentColor> take, List<StudentColor> give) throws GameRuleException {
        characterSevenValidator(playingWizard, take, give);
        useCard(playingWizard);
        studentColorList.removeAll(take);
        studentColorList.addAll(give);
        Game.request(gameId).getWizard(playingWizard).getEntranceStudents().removeAll(give);
        Game.request(gameId).getWizard(playingWizard).getEntranceStudents().addAll(take);
    }

    /**
     * Validator for character seven useEffect method
     * @param playingWizard the player playing the card
     * @param take students to take from the card
     * @param give students to take from the entrance
     * @throws GameRuleException if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterSevenValidator(Integer playingWizard, List<StudentColor> take, List<StudentColor> give) throws GameRuleException {
        characterValidator(playingWizard);
        List<StudentColor> entrance = Game.request(gameId).getWizard(playingWizard).getEntranceStudents();
        for (StudentColor color : StudentColor.values()) {
            if (take.stream().filter(c -> color == c).count() > studentColorList.stream().filter(c -> color == c).count())
                throw new GameRuleException("Students not available on the card");
            if (give.stream().filter(c -> color == c).count() > entrance.stream().filter(c -> color == c).count())
                throw new GameRuleException("Students not available in the entrance");
        }
        if(give.size()>3 || give.size() != take.size()) throw new GameRuleException("Inexact number of students");
    }

    public List<StudentColor> getStudentColorList () {
        return studentColorList;
    }
}

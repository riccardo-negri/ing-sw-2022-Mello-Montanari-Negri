package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

import java.util.List;

public class CharacterTen extends Character {

    public CharacterTen (Integer gameId, Integer characterId) {
        super(gameId, characterId, 1);
    }

    /**
     * you can exchange up to two students between your dining room and your entrance
     * @param playingWizard the player playing the card
     * @param take students to take from your entrance
     * @param give students to take from your dining room
     */
    public void useEffect(Integer playingWizard, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterTenValidator(playingWizard, take, give);
        useCard(playingWizard);
        for (StudentColor color : give) Game.request(gameId).getWizard(playingWizard).takeDiningStudent(color);
        for (StudentColor color : take) Game.request(gameId).getWizard(playingWizard).putDiningStudent(color);
        Game.request(gameId).getWizard(playingWizard).getEntranceStudents().removeAll(take);
        Game.request(gameId).getWizard(playingWizard).getEntranceStudents().addAll(give);
    }

    /**
     * Validator for character ten useEffect method
     * @param playingWizard the player playing the card
     * @param take students to take from your entrance
     * @param give students to take from your dining room
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterTenValidator(Integer playingWizard, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterValidator(playingWizard);
        if (!Game.request(gameId).getWizard(playingWizard).getEntranceStudents().containsAll(take))
            throw new Exception("Students not present in entrance");
        for (StudentColor color : StudentColor.values())
            if (give.stream().filter(x -> x == color).count() > Game.request(gameId).getWizard(playingWizard).getDiningStudents(color))
                throw new Exception("Students not present in dining room");
        if (take.size() > 2 || take.size() != give.size()) throw new Exception("Inexact number of students");
    }
}

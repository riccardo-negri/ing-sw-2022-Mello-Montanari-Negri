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
     * @param playingTower the player playing the card
     * @param take students to take from your entrance
     * @param give students to take from your dining room
     */
    public void useEffect(Tower playingTower, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterTenValidator(playingTower, take, give);
        useCard(playingTower);
        for (StudentColor color : give) Game.request(gameId).getWizard(playingTower).takeDiningStudent(color);
        for (StudentColor color : take) Game.request(gameId).getWizard(playingTower).putDiningStudent(color);
        Game.request(gameId).getWizard(playingTower).getEntranceStudents().removeAll(take);
        Game.request(gameId).getWizard(playingTower).getEntranceStudents().addAll(give);
    }

    /**
     * Validator for character ten useEffect method
     * @param playingTower the player playing the card
     * @param take students to take from your entrance
     * @param give students to take from your dining room
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterTenValidator(Tower playingTower, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterValidator(playingTower);
        if (!Game.request(gameId).getWizard(playingTower).getEntranceStudents().containsAll(take))
            throw new Exception("Students not present in entrance");
        for (StudentColor color : StudentColor.values())
            if (give.stream().filter(x -> x == color).count() > Game.request(gameId).getWizard(playingTower).getDiningStudents(color))
                throw new Exception("Students not present in dining room");
        if (take.size() > 2 || take.size() != give.size()) throw new Exception("Inexact number of students");
    }
}

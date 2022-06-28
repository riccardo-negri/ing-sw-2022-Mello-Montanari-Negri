package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.GameRuleException;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

/**
 * character 10 class with logic
 */
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
    public void useEffect(Integer playingWizard, List<StudentColor> take, List<StudentColor> give) throws GameRuleException {
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
     * @throws GameRuleException if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterTenValidator(Integer playingWizard, List<StudentColor> take, List<StudentColor> give) throws GameRuleException {
        characterValidator(playingWizard);
        Wizard player = Game.request(gameId).getWizard(playingWizard);
        for (StudentColor color : StudentColor.values()) {
            if (take.stream().filter(c -> c==color).count() > player.getEntranceStudents().stream().filter(c -> c==color).count())
                throw new GameRuleException("Students not present in entrance");
            if (give.stream().filter(c -> c==color).count() > player.getDiningStudents(color))
                throw new GameRuleException("Students not present in dining room");
            if (player.getDiningStudents(color) + take.stream().filter(c -> c==color).count() - give.stream().filter(c -> c==color).count() > 10)
                throw new GameRuleException("Not enough free spots in dining room");
        }
        if (take.size() > 2 || take.size() != give.size()) throw new GameRuleException("Inexact number of students");
    }
}

package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.List;

public class CharacterTen extends Character {

    public CharacterTen () {
        super(1);
    }

    /**
     * you can exchange up to two students between your dining room and your entrance
     * @param player the player playing the card
     * @param game the current game
     * @param take students to take from your entrance
     * @param give students to take from your dining room
     */
    public void useEffect(Wizard player, Game game, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterTenValidator(player, game, take, give);
        useCard(player);
        for (StudentColor color : give) player.takeDiningStudent(color);
        for (StudentColor color : take) player.putDiningStudent(color);
        player.getEntranceStudents().removeAll(take);
        player.getEntranceStudents().addAll(give);
    }

    /**
     * Validator for character ten useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @param take students to take from your entrance
     * @param give students to take from your dining room
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * the students he is asking are not available, or he is asking an incorrect number of students
     */
    public void characterTenValidator(Wizard player, Game game, List<StudentColor> take, List<StudentColor> give) throws Exception {
        characterValidator(player, game);
        if (!player.getEntranceStudents().containsAll(take)) throw new Exception("Students not present in entrance");
        for (StudentColor color : StudentColor.values())
            if (give.stream().filter(x -> x == color).count() > player.getDiningStudents(color)) throw new Exception("Students not present in dining room");
        if (take.size() > 2 || take.size() != give.size()) throw new Exception("Inexact number of students");
    }
}
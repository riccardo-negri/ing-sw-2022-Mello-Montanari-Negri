package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.characters.CharacterEight;
import it.polimi.ingsw.model.entity.characters.CharacterNine;
import it.polimi.ingsw.model.entity.characters.CharacterSix;
import it.polimi.ingsw.model.enums.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * group of island with the same tower color
 */
public class IslandGroup {

    private final Integer islandGroupId;
    private final List<Island> islandList;
    private Tower tower;

    public IslandGroup(List<StudentColor> studentColors, Integer islandGroupId) {
        islandList = new ArrayList<>();
        islandList.add(new Island(studentColors, islandGroupId));
        this.islandGroupId = islandGroupId;
    }

    public List<Island> getIslandList() { return islandList; }

    /**
     * updates the color of the tower on the island, to be called after a move of mother nature to calculate the new owner of the island
     * @param game the game, used to change the other classes
     */
    public void updateTower (Game game, Character activatedCharacter) {

        if (getIslandList().stream().anyMatch(Island::isStopCard))
            getIslandList().stream().filter(Island::isStopCard).findFirst().get().removeStopCard();
        else {
            Integer[] values = new Integer[game.getPlayerNumber().getWizardNumber()];
            for (int i = 0; i < game.getPlayerNumber().getWizardNumber(); i++) {
                values[i] = 0;
            }

            if (activatedCharacter instanceof CharacterEight) values[activatedCharacter.getActivator().getTowerColor().getValue()] += 2;

            if (tower != null && !(activatedCharacter instanceof CharacterSix)) values[tower.getValue()] += islandList.size();

            for (StudentColor color : StudentColor.values()) {
                Wizard w;
                if ((!(activatedCharacter instanceof CharacterNine) || ((CharacterNine) activatedCharacter).getStudentColor() != color)
                        && (w = game.getProfessor(color).getMaster(activatedCharacter)) != null) {
                    values[w.getTowerColor().getValue()] += islandList.stream()
                        .flatMap(x -> x.getStudentColorList().stream())
                        .filter(x -> x == color).collect(Collectors.toList()).size();
                }
            }

            for (int i = 0; i < values.length; i++) {
                boolean max = true;
                for (int j = 0; j < values.length; j++) {
                    if (i != j && values[i] <= values[j]) {
                        max = false;
                        break;
                    }
                }
                if (max) {
                    changeTower(Tower.fromNumber(i), game);
                    break;
                }
            }
        }
    }

    /**
     * changes the tower on the island and update the count of the wizards
     * @param newOwner tower color of the new wizard
     * @param game game, used to reach the wizards
     */
    private void changeTower(Tower newOwner, Game game) {
        game.getWizardsFromTower(tower).stream().forEach(x -> x.changeTowerNumber(islandList.size()));
        game.getWizardsFromTower(newOwner).stream().forEach(x -> x.changeTowerNumber(-islandList.size()));
        tower = newOwner;
    }

    public Tower getTower() { return tower; }

    public Integer getId() { return islandGroupId; }
}

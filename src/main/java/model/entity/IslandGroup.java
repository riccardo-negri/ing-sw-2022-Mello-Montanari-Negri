package model.entity;

import model.entity.characters.Character;
import model.entity.characters.CharacterSix;
import model.enums.StudentColor;
import model.enums.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * group of island with the same tower color
 */
public class IslandGroup {

    private final List<Island> islandList;
    private Tower tower;

    public IslandGroup(List<StudentColor> studentColors) {
        islandList = new ArrayList<>();
        islandList.add(new Island(studentColors));
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

            if (tower != null && !(activatedCharacter instanceof CharacterSix)) values[tower.getValue()] += islandList.size();

            for (StudentColor color : StudentColor.values()) {
                Wizard w;
                if((w = game.getProfessor(color).getMaster(activatedCharacter)) != null) {
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
        game.getWizard(tower).changeTowerNumber(islandList.size());
        game.getWizard(newOwner).changeTowerNumber(-islandList.size());
        tower = newOwner;
    }

    public Tower getTower() { return tower; }
}

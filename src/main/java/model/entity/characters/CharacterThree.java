package model.entity.characters;

import model.entity.Game;
import model.entity.IslandGroup;
import model.entity.Wizard;

public class CharacterThree extends  Character{

    public CharacterThree() {
        super(3);
    }

    public void useEffect(Wizard player, Game game, IslandGroup islandGroup) throws Exception {
        useCard(player);
        islandGroup.updateTower(game, null);
    }
}

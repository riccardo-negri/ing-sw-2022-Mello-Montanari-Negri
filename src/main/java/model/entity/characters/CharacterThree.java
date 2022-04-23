package model.entity.characters;

import model.entity.Game;
import model.entity.IslandGroup;
import model.entity.Wizard;

public class CharacterThree extends  Character{

    public CharacterThree() {
        super(3);
    }

    @Override
    public void useEffect(Wizard player, Game game, Object request) throws Exception {
        if (! (request instanceof IslandGroup)) throw new Exception("Bad request: not a island group");
        useCard(player);
        IslandGroup group = (IslandGroup) request;
        group.updateTower(game, false, null);
    }
}

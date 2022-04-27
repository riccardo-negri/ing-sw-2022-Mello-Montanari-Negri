package model.entity.characters;

import model.entity.Game;
import model.entity.IslandGroup;
import model.entity.Wizard;

public class CharacterThree extends  Character{

    public CharacterThree() {
        super(3);
    }

    /**
     * update the owner of an island group without the need of mother nature
     * @param player the player playing the card
     * @param game the current game
     * @param islandGroup the group to update the owner on
     * @throws Exception
     */
    public void useEffect(Wizard player, Game game, IslandGroup islandGroup) throws Exception {
        characterThreeValidator(player, game, islandGroup);
        useCard(player);
        islandGroup.updateTower(game, null);
        game.unifyIslands();
    }

    /**
     * validator for character three useEffect method
     * @param player the player playing the card
     * @param game the current game
     * @param islandGroup the group to update the owner on
     * @throws Exception if it is not the player turn, he does not have enough money to activate the card,
     * or the island group does not exist
     */
    public void characterThreeValidator(Wizard player, Game game, IslandGroup islandGroup) throws Exception {
        characterValidator(player, game);
        if (!game.getIslandGroupList().contains(islandGroup)) throw new Exception("Non existing island group");
    }
}

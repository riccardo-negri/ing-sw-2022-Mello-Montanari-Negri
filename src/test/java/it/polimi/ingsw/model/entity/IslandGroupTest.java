package it.polimi.ingsw.model.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class IslandGroupTest {

    Game game;

    @Test
    void updateTower() {
        Assertions.assertDoesNotThrow(() -> {
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/update_tower_test/Test1Before.json"));
            for (IslandGroup g : game.getIslandGroupList())
                g.updateTower(game, null);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/update_tower_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/update_tower_test/Test1After1.json")).readLine(), game.serializeGame());
        });
    }
}
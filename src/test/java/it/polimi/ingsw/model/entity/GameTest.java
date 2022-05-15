package it.polimi.ingsw.model.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;


class GameTest {

    Game game;
    @Test
    void SerializationTest() throws Exception {
        //Game game = Game.request(Game.gameEntityFactory(GameMode.COMPLETE, PlayerNumber.FOUR));
        //game.serializeGame("./test1Before.json");
    }

    @Test
    void DeserializationTest() {
        //Game.deserializeGame("./data/Test1Before.json");
    }

    @Test
    void doMotherNatureSteps() {
        Assertions.assertDoesNotThrow(() -> {
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1Before.json"));
            game.doMotherNatureSteps(2);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json")).readLine(), game.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            game.doMotherNatureSteps(12);
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json")).readLine(), game.serializeGame());
        });

    }

    @Test
    void unifyIslands() {
        Assertions.assertDoesNotThrow(() -> {
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1Before.json"));
            game.unifyIslands();
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1After1.json")).readLine(), game.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2Before.json"));
            game.unifyIslands();
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2After1.json")).readLine(), game.serializeGame());
        });
    }
}
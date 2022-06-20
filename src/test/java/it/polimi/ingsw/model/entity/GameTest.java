package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.entity.gameState.ChooseCloudActionState;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;


class GameTest {

    Game game1, game2;

    @Test
    void doMotherNatureSteps() {
        Assertions.assertDoesNotThrow(() -> {
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1Before.json"));
            game1.doMotherNatureSteps(2);
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json")).readLine(), game1.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            game1.doMotherNatureSteps(12);
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json")).readLine(), game1.serializeGame());
        });

    }

    @Test
    void unifyIslands() {
        Assertions.assertDoesNotThrow(() -> {
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1Before.json"));
            game1.unifyIslands();
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1After1.json")).readLine(), game1.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2Before.json"));
            game1.unifyIslands();
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2After1.json")).readLine(), game1.serializeGame());
        });
    }

    @Test
    void gameEnd() {
        try{ game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test1Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
        Assertions.assertDoesNotThrow(() -> {
            ((ChooseCloudActionState) game2.getGameState()).chooseCloud(0, 1);
            game2.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test1After1.json")).readLine(), game2.serializeGame());
        });
        try{ game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test2Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
        Assertions.assertDoesNotThrow(() -> {
            ((ChooseCloudActionState) game2.getGameState()).chooseCloud(0, 1);
            game2.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test2After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test2After1.json")).readLine(), game2.serializeGame());
        });
        try{ game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test3Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
        Assertions.assertDoesNotThrow(() -> {
            ((MoveMotherNatureActionState) game2.getGameState()).moveMotherNature(1, 4);
            game2.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test3After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test3After1.json")).readLine(), game2.serializeGame());
        });
    }
}
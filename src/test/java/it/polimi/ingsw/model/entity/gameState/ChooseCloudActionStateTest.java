package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class ChooseCloudActionStateTest {

    Game game1, game2;

    @BeforeEach
    void initializeGame() {
        try{
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/choose_cloud_test/Test1Before.json"));
            game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/choose_cloud_test/Test2Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void chooseCloud() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                ((ChooseCloudActionState) game1.getGameState()).chooseCloudValidator(0, 1)
        ).getMessage());
        Assertions.assertEquals("Chosen cloud not present", Assertions.assertThrows(Exception.class, () ->
                ((ChooseCloudActionState) game1.getGameState()).chooseCloudValidator(2, 10)
        ).getMessage());
        Assertions.assertEquals("Chosen cloud already taken", Assertions.assertThrows(Exception.class, () ->
                ((ChooseCloudActionState) game1.getGameState()).chooseCloudValidator(2, 0)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((ChooseCloudActionState) game1.getGameState()).chooseCloud(2, 1);
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/choose_cloud_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/choose_cloud_test/Test1After1.json")).readLine(), game1.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            ((ChooseCloudActionState) game2.getGameState()).chooseCloud(0, 1);
            Assertions.assertEquals(game2.getGameState().getClass(), PlanningState.class);
        });
    }
}
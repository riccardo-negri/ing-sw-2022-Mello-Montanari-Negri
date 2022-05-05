package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.Tower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;


class PlanningStateTest {

    Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void selectCard() {
        initializeGame();
        Assertions.assertDoesNotThrow(() -> {
            ((PlanningState) game.getGameState()).selectCard(2, 2);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After1.json")).readLine(), game.serializeGame());
        });
        Assertions.assertEquals("Card already played with valid alternatives", Assertions.assertThrows(Exception.class, () ->
            ((PlanningState) game.getGameState()).cardSelectionValidator(0, 2)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((PlanningState) game.getGameState()).selectCard(0, 3);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After2.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After2.json")).readLine(), game.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            ((PlanningState) game.getGameState()).selectCard(3, 10);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After3.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After3.json")).readLine(), game.serializeGame());
        });
        Assertions.assertEquals("Card already played with valid alternatives", Assertions.assertThrows(Exception.class, () ->
            ((PlanningState) game.getGameState()).cardSelectionValidator(1, 10)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((PlanningState) game.getGameState()).selectCard(1, 6);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After4.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planning_state_test/Test1After4.json")).readLine(), game.serializeGame());
        });
    }
}
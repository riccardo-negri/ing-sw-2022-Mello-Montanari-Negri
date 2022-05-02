package it.polimi.ingsw.model.entity.gameState;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.Tower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanningStateTest {

    Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/planningStateTest.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void selectCard() {
        Assertions.assertDoesNotThrow(() -> {
            ((PlanningState) game.getGameState()).selectCard(Tower.WHITE, 2);
            game.serializeGame("./data/out.json");
        });
    }

    @Test
    void cardSelectionValidator() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () -> {
            ((PlanningState) game.getGameState()).cardSelectionValidator(Tower.BLACK, 1);
        }).getMessage());
        Assertions.assertDoesNotThrow(() -> ((PlanningState) game.getGameState()).cardSelectionValidator(Tower.WHITE, 2));
    }
}
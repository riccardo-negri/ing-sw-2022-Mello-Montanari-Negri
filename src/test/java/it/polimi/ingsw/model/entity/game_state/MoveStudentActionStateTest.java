package it.polimi.ingsw.model.entity.game_state;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class MoveStudentActionStateTest {

    Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void moveStudentToDiningRoom() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoomValidator(0, StudentColor.BLUE)
        ).getMessage());
        Assertions.assertEquals("No students available in the selected color", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(2, StudentColor.BLUE)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(2, StudentColor.YELLOW);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1After1.json")).readLine(), game.serializeGame());
        });
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoomValidator(3, StudentColor.BLUE)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(2, StudentColor.PINK);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1After2.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1After2.json")).readLine(), game.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            ((MoveStudentActionState) game.getGameState()).moveStudentToDiningRoom(2, StudentColor.RED);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1After3.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test1After3.json")).readLine(), game.serializeGame());
        });
    }

    @Test
    void moveStudentToIsland() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToIslandValidator(0, StudentColor.BLUE, 1)
        ).getMessage());
        Assertions.assertEquals("No students available in the selected color", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(2, StudentColor.BLUE, 1)
        ).getMessage());
        Assertions.assertEquals("Island not found", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(2, StudentColor.YELLOW, 13)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(2, StudentColor.YELLOW, 1);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test2After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test2After1.json")).readLine(), game.serializeGame());
        });
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(3, StudentColor.BLUE, 1)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(2, StudentColor.PINK, 5);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test2After2.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test2After2.json")).readLine(), game.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            ((MoveStudentActionState) game.getGameState()).moveStudentToIsland(2, StudentColor.RED, 3);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test2After3.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/move_student_test/Test2After3.json")).readLine(), game.serializeGame());
        });
    }
}
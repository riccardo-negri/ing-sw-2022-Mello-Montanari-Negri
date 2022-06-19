package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSevenTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character7TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(7).characterValidator(1)
        ).getMessage());
        Assertions.assertEquals("Students not available in the entrance", Assertions.assertThrows(Exception.class, () ->
                ((CharacterSeven) game.getCharacter(7)).characterSevenValidator(0, Arrays.asList(StudentColor.GREEN, StudentColor.GREEN, StudentColor.YELLOW), Arrays.asList(StudentColor.RED, StudentColor.YELLOW, StudentColor.YELLOW))
        ).getMessage());
        Assertions.assertEquals("Students not available on the card", Assertions.assertThrows(Exception.class, () ->
                ((CharacterSeven) game.getCharacter(7)).characterSevenValidator(0, Arrays.asList(StudentColor.BLUE, StudentColor.PINK, StudentColor.RED), Arrays.asList(StudentColor.RED, StudentColor.RED, StudentColor.PINK))
        ).getMessage());
        Assertions.assertEquals("Inexact number of students", Assertions.assertThrows(Exception.class, () ->
                ((CharacterSeven) game.getCharacter(7)).characterSevenValidator(0, Arrays.asList(StudentColor.PINK, StudentColor.RED), Arrays.asList(StudentColor.RED, StudentColor.RED, StudentColor.PINK))
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterSeven) game.getCharacter(7)).useEffect(0, Arrays.asList(StudentColor.GREEN, StudentColor.GREEN, StudentColor.YELLOW), Arrays.asList(StudentColor.RED, StudentColor.RED, StudentColor.RED));
            game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character7TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character7TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
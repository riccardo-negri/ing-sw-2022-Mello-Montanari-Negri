package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTenTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character10TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(10).characterValidator(0)
        ).getMessage());
        Assertions.assertEquals("Not enough money to activate the effect", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(11).characterValidator(1)
        ).getMessage());
        Assertions.assertEquals("Students not present in dining room", Assertions.assertThrows(Exception.class, () ->
                ((CharacterTen) game.getCharacter(10)).characterTenValidator(1, Arrays.asList(StudentColor.PINK, StudentColor.YELLOW), Arrays.asList(StudentColor.GREEN, StudentColor.GREEN))
        ).getMessage());
        Assertions.assertEquals("Students not present in entrance", Assertions.assertThrows(Exception.class, () ->
                ((CharacterTen) game.getCharacter(10)).characterTenValidator(1, Arrays.asList(StudentColor.RED, StudentColor.RED), Arrays.asList(StudentColor.BLUE, StudentColor.BLUE))
        ).getMessage());
        Assertions.assertEquals("Inexact number of students", Assertions.assertThrows(Exception.class, () ->
                ((CharacterTen) game.getCharacter(10)).characterTenValidator(1, List.of(StudentColor.YELLOW), Arrays.asList(StudentColor.YELLOW, StudentColor.YELLOW))
        ).getMessage());
        Assertions.assertEquals("Not enough free spots in dining room", Assertions.assertThrows(Exception.class, () ->
                ((CharacterTen) game.getCharacter(10)).characterTenValidator(1, Arrays.asList(StudentColor.YELLOW, StudentColor.YELLOW), Arrays.asList(StudentColor.BLUE, StudentColor.BLUE))
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterTen) game.getCharacter(10)).useEffect(1, Arrays.asList(StudentColor.RED, StudentColor.GREEN), Arrays.asList(StudentColor.YELLOW, StudentColor.YELLOW));
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character10TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character10TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
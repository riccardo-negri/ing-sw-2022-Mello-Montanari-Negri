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

class CharacterElevenTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character11TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(11).characterValidator(0)
        ).getMessage());
        Assertions.assertEquals("Student color not present on the card", Assertions.assertThrows(Exception.class, () ->
                ((CharacterEleven) game.getCharacter(11)).characterElevenValidator(1, StudentColor.RED)
        ).getMessage());
        Assertions.assertEquals("The dining room is full", Assertions.assertThrows(Exception.class, () ->
                ((CharacterEleven) game.getCharacter(11)).characterElevenValidator(1, StudentColor.YELLOW)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            int precNumber = game.getWizard(1).getDiningStudents(StudentColor.BLUE);
            ((CharacterEleven) game.getCharacter(11)).useEffect(1, StudentColor.BLUE);
            Assertions.assertEquals(game.getWizard(1).getDiningStudents(StudentColor.BLUE), precNumber + 1);
        });
    }
}
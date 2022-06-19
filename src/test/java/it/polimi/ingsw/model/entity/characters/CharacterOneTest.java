package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class CharacterOneTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character1TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(1).characterValidator(1)
        ).getMessage());
        Assertions.assertEquals("Student color not present on the card", Assertions.assertThrows(Exception.class, () ->
                ((CharacterOne) game.getCharacter(1)).characterOneValidator(0, StudentColor.RED, 1)
        ).getMessage());
        Assertions.assertEquals("Selected island doesn't exist", Assertions.assertThrows(Exception.class, () ->
                ((CharacterOne) game.getCharacter(1)).characterOneValidator(0, StudentColor.BLUE, 13)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterOne) game.getCharacter(1)).characterOneValidator(0, StudentColor.BLUE, 10);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character1TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character1TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
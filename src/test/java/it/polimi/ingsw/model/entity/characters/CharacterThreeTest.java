package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class CharacterThreeTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character3TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(3).characterValidator(0)
        ).getMessage());
        Assertions.assertEquals("Selected island group doesn't exist", Assertions.assertThrows(Exception.class, () ->
                ((CharacterThree) game.getCharacter(3)).characterThreeValidator(1, 15)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterThree) game.getCharacter(3)).useEffect(1, 4);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character3TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character3TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
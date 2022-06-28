package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class CharacterTwelveTest {

    private Game game;
    private CharacterTwelve characterTwelve;

    @BeforeEach
    void initializeGame() {
        try{
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character12TestBefore.json"));
            characterTwelve = (CharacterTwelve) game.getCharacter(12);
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                characterTwelve.characterValidator(0)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            characterTwelve.useEffect(1, StudentColor.YELLOW);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character12TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character12TestAfter.json")).readLine(), game.serializeGame());
        });
        Assertions.assertEquals("A character is already active", Assertions.assertThrows(Exception.class, () ->
                characterTwelve.characterValidator(1)
        ).getMessage());
    }
}
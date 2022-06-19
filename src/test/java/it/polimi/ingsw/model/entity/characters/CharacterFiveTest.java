package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class CharacterFiveTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = it.polimi.ingsw.model.entity.Game.request(it.polimi.ingsw.model.entity.Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character5TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(5).characterValidator(0)
        ).getMessage());
        Assertions.assertEquals("Island does not exist", Assertions.assertThrows(Exception.class, () ->
                ((CharacterFive) game.getCharacter(5)).characterFiveValidator(1, 15)
        ).getMessage());
        Assertions.assertEquals("No more stop cards available", Assertions.assertThrows(Exception.class, () -> {
            Game game1 = it.polimi.ingsw.model.entity.Game.request(it.polimi.ingsw.model.entity.Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character5.1TestBefore.json"));
            ((CharacterFive) game1.getCharacter(5)).characterFiveValidator(1, 5);
        }).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterFive) game.getCharacter(5)).useEffect(1, 1);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character5TestAfter1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character5TestAfter1.json")).readLine(), game.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(1, 1);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character5TestAfter2.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character5TestAfter2.json")).readLine(), game.serializeGame());
        });
    }
}
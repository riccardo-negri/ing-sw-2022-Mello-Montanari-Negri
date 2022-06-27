package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.game_state.MoveMotherNatureActionState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CharacterEightTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character8TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(8).characterValidator(0)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterEight) game.getCharacter(8)).useEffect(1);
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(1, 2);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character8TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character8TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
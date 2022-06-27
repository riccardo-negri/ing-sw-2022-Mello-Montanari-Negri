package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.game_state.MoveMotherNatureActionState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSixTest {

    private Game game;

    @BeforeEach
    void initializeGame() {
        try{ game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character6TestBefore.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(6).characterValidator(0)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            ((CharacterSix) game.getCharacter(6)).useEffect(1);
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(1, 2);
            //game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character6TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character6TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
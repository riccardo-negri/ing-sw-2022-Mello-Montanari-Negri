package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.game_state.MoveMotherNatureActionState;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;

class CharacterNineTest {

    private Game game;
    private CharacterNine characterNine;

    @BeforeEach
    void initializeGame() {
        try{
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character9TestBefore.json"));
            characterNine = (CharacterNine) game.getCharacter(9);
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                characterNine.characterValidator(0)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            characterNine.useEffect(1, StudentColor.RED);
            ((MoveMotherNatureActionState) game.getGameState()).moveMotherNature(1, 2);
            game.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character9TestAfter.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character9TestAfter.json")).readLine(), game.serializeGame());
        });
    }
}
package it.polimi.ingsw.model.entity.characters;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class CharacterOneTest {

    private Game game;
    private CharacterOne characterOne;
    private Game game1;
    private Character character1;

    @BeforeEach
    void initializeGame() {
        try{
            game = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/character_test/character1TestBefore.json"));
            characterOne = (CharacterOne) game.getCharacter(1);
            game1 = Game.request(Game.gameEntityFactory(GameMode.COMPLETE, PlayerNumber.TWO));
            character1 = game1.getCharacters()[0];
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
    }

    @Test
    void useEffect() {
        Assertions.assertEquals("Wrong game phase", Assertions.assertThrows(Exception.class, () ->
            character1.characterValidator(game1.getGameState().getCurrentPlayer())
        ).getMessage());
        Assertions.assertEquals("Character not found", Assertions.assertThrows(Exception.class, () ->
                game.getCharacter(10)
        ).getMessage());
        Assertions.assertEquals("Wrong player", Assertions.assertThrows(Exception.class, () ->
                characterOne.characterValidator(1)
        ).getMessage());
        Assertions.assertEquals("Student color not present on the card", Assertions.assertThrows(Exception.class, () ->
                characterOne.characterOneValidator(0, StudentColor.RED, 1)
        ).getMessage());
        Assertions.assertEquals("Selected island doesn't exist", Assertions.assertThrows(Exception.class, () ->
                characterOne.characterOneValidator(0, StudentColor.BLUE, 13)
        ).getMessage());
        Assertions.assertDoesNotThrow(() -> {
            List<StudentColor> studentListBefore = new ArrayList<>(game.getIsland(10).getStudentColorList());
            studentListBefore.add(StudentColor.BLUE);
            characterOne.useEffect(0, StudentColor.BLUE, 10);
            Assertions.assertEquals( studentListBefore, game.getIsland(10).getStudentColorList());
        });
    }
}
package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.gameState.ChooseCloudActionState;
import it.polimi.ingsw.model.entity.gameState.MoveMotherNatureActionState;
import it.polimi.ingsw.model.entity.gameState.PlanningState;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.StudentColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;


class GameTest {

    Game game1, game2;

    @Test
    void doMotherNatureSteps() {
        Assertions.assertDoesNotThrow(() -> {
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1Before.json"));
            game1.doMotherNatureSteps(2);
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json")).readLine(), game1.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            game1.doMotherNatureSteps(12);
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/mother_nature_step_test/Test1After1.json")).readLine(), game1.serializeGame());
        });

    }

    @Test
    void unifyIslands() {
        Assertions.assertDoesNotThrow(() -> {
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1Before.json"));
            game1.unifyIslands();
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test1After1.json")).readLine(), game1.serializeGame());
        });
        Assertions.assertDoesNotThrow(() -> {
            game1 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2Before.json"));
            game1.unifyIslands();
            //game1.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/unify_islands_test/Test2After1.json")).readLine(), game1.serializeGame());
        });
    }

    @Test
    void gameEnd() {
        try{ game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test1Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
        Assertions.assertDoesNotThrow(() -> {
            ((ChooseCloudActionState) game2.getGameState()).chooseCloud(0, 1);
            game2.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test1After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test1After1.json")).readLine(), game2.serializeGame());
        });
        try{ game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test2Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
        Assertions.assertDoesNotThrow(() -> {
            ((ChooseCloudActionState) game2.getGameState()).chooseCloud(0, 1);
            game2.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test2After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test2After1.json")).readLine(), game2.serializeGame());
        });
        try{ game2 = Game.request(Game.deserializeGameFromFile("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test3Before.json"));
        } catch (Exception e) { System.err.println("Can't find file in test initialization..."); }
        Assertions.assertDoesNotThrow(() -> {
            ((MoveMotherNatureActionState) game2.getGameState()).moveMotherNature(1, 4);
            game2.serializeGame("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test3After1.json");
            Assertions.assertEquals( new BufferedReader(new FileReader("./src/test/java/it/polimi/ingsw/model/entity/serialized_tests/game_end_test/Test3After1.json")).readLine(), game2.serializeGame());
        });
    }

    @Test
    void characterCreationTest() {
        int[] createdCharacters = new int[12];
        while (Arrays.stream(createdCharacters).anyMatch(created -> created == 0)) {
            Game game = Game.request(Game.gameEntityFactory(GameMode.COMPLETE, PlayerNumber.TWO));
            Arrays.stream(game.getCharacters()).forEach(c -> createdCharacters[c.getId()-1] += 1);
            Assertions.assertEquals(Arrays.stream(game.getCharacters()).mapToInt(Character::getId).distinct().count(), 3);
        }
        Game game = Game.request(Game.gameEntityFactory(GameMode.EASY, PlayerNumber.TWO));
        Assertions.assertNull(game.getCharacters());
    }

    @Test
    void initialIslandStudentsTest() {
        Game game = Game.request(Game.gameEntityFactory(GameMode.COMPLETE, PlayerNumber.TWO));
        for (StudentColor s : StudentColor.values())
            Assertions.assertEquals(game.getIslandGroupList().stream().flatMap(islandGroup -> islandGroup.getIslandList().stream())
                    .flatMap(island -> island.getStudentColorList().stream()).filter(color -> color == s).count(), 2);

        for (int i=0; i<12; i++)
            Assertions.assertEquals(game.getIsland(i).getStudentColorList().size(), i%6==0 ? 0: 1);
    }

    @Test
    void playerNumberTest() {
        for (PlayerNumber n : PlayerNumber.values()) {
            Game game = Game.request(Game.gameEntityFactory(GameMode.EASY, n));
            Assertions.assertEquals(game.getAllWizards().size(), n.getWizardNumber());
            Assertions.assertEquals(game.getCloudList().size(), n.getWizardNumber());
            game.getCloudList().stream().forEach(cloud ->
                    Assertions.assertEquals(cloud.getCloudContent().size(), n.getCloudSize()));
        }
    }

}
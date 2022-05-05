package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.Type;
import org.junit.jupiter.api.Test;


class GameTest {
    @Test
    void SerializationTest() throws Exception{
        //Game game = Game.request(Game.gameEntityFactory(GameMode.COMPLETE, PlayerNumber.FOUR, Type.SERVER));
        //game.serializeGame("./data/Test1Before.json");
    }

    @Test
    void DeserializationTest() throws Exception{
        //Game.deserializeGame("./data/Test1Before.json");
    }

}
package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.Disconnected;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.UserConnected;
import it.polimi.ingsw.networking.moves.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class EndGameTest {

    Logger logger = LogFormatter.getLogger("Test");

    Connection tom;
    Connection ric;
    Connection pit;

    @BeforeEach
    void clearSaves() throws IOException {
        Procedures.clearFolder(logger);
    }

    void waitOnAll(Class<?> filter) {
        tom.waitMessage(filter);
        ric.waitMessage(filter);
        pit.waitMessage(filter);
    }

    void waitOnAllWithCheck(Class<?> filter) {
        waitOnAll(filter);
        assert tom.noMessageLeft();
        assert ric.noMessageLeft();
        assert pit.noMessageLeft();
    }

    @Test
    void lastMoveTest() throws IOException, InterruptedException {
        Procedures.clearFolder(logger);
        String code = "#4Eg";
        String state = """
                {"gameMode":"COMPLETE","playerNumber":"THREE","wizardList":[{"wizardId":0,"money":2,"cardDeck":{"assistantCards":["FIVE","SEVEN","NINE"]},"entranceStudents":["RED","RED","RED","PINK","RED","GREEN","BLUE","BLUE","BLUE"],"diningStudents":[7,5,6,0,6],"towerColor":"BLACK","towerNumber":1},{"wizardId":1,"money":6,"cardDeck":{"assistantCards":["ONE","NINE","TEN"]},"entranceStudents":["YELLOW","BLUE","YELLOW","RED","PINK","BLUE","RED","PINK","YELLOW"],"diningStudents":[3,4,7,6,7],"towerColor":"WHITE","towerNumber":3},{"wizardId":2,"money":5,"cardDeck":{"assistantCards":["SIX","EIGHT","NINE"]},"entranceStudents":["PINK","PINK","YELLOW","YELLOW","YELLOW","YELLOW","PINK","RED","BLUE"],"diningStudents":[3,6,6,7,0],"towerColor":"GRAY","towerNumber":2}],"professors":[{"color":"YELLOW","master":0},{"color":"BLUE","master":2},{"color":"GREEN","master":1},{"color":"RED","master":2},{"color":"PINK","master":1}],"islandGroupList":[{"islandGroupId":0,"islandList":[{"islandId":10,"stopCard":false,"studentColorList":["GREEN"]},{"islandId":11,"stopCard":false,"studentColorList":["GREEN"]},{"islandId":0,"stopCard":false,"studentColorList":["RED","PINK","BLUE"]}],"tower":"GRAY"},{"islandGroupId":1,"islandList":[{"islandId":1,"stopCard":false,"studentColorList":["PINK","GREEN","RED"]}],"tower":"WHITE"},{"islandGroupId":2,"islandList":[{"islandId":2,"stopCard":false,"studentColorList":["BLUE","GREEN","YELLOW","YELLOW"]}],"tower":"BLACK"},{"islandGroupId":3,"islandList":[{"islandId":3,"stopCard":false,"studentColorList":["BLUE"]}],"tower":"GRAY"},{"islandGroupId":4,"islandList":[{"islandId":4,"stopCard":false,"studentColorList":["RED"]}],"tower":"WHITE"},{"islandGroupId":8,"islandList":[{"islandId":5,"stopCard":false,"studentColorList":["YELLOW"]},{"islandId":6,"stopCard":false,"studentColorList":["YELLOW"]},{"islandId":7,"stopCard":false,"characterFive":{"stopNumber":4,"characterId":5,"activator":0,"price":2,"used":true},"studentColorList":["RED","PINK","PINK"]},{"islandId":8,"stopCard":false,"studentColorList":["YELLOW","GREEN","YELLOW"]}],"tower":"BLACK"},{"islandGroupId":9,"islandList":[{"islandId":9,"stopCard":false,"studentColorList":["PINK","RED","GREEN"]}],"tower":"WHITE"}],"cloudList":[{"cloudId":0,"playerNumber":"THREE","studentColorList":["BLUE"],"taken":false},{"cloudId":1,"playerNumber":"THREE","studentColorList":[],"taken":false},{"cloudId":2,"playerNumber":"THREE","studentColorList":[],"taken":false}],"characters":[{"studentColorList":["PINK","RED","BLUE","PINK"],"characterId":1,"activator":0,"price":1,"used":true},{"characterId":3,"activator":2,"price":3,"used":true},{"stopNumber":4,"characterId":5,"activator":0,"price":2,"used":true}],"bag":{"studentListIn":[],"studentListOut":[],"colorNumber":[0,0,0,0,0],"total":0,"empty":true},"gameState":{"gameState":"PS","playerOrder":[1,2,0],"currentlyPlaying":0},"gameEnded":false}""";

        MainSavesManager msm =  new MainSavesManager(logger);
        msm.createFolder(SavesManager.gameFolderPath(code));

        msm.writeFile(SavesManager.gameFilePath(code, "usernames"), "pietro\nriccardo\ntommaso");
        msm.writeFile(SavesManager.gameFilePath(code, "153"), state);

        MatchmakingServer s = new MatchmakingServer();
        assert s.socket != null;
        Thread t = new Thread(s::run);
        t.start();

        TimeUnit.MILLISECONDS.sleep(100);
        tom = Procedures.reconnectLogin("tommaso", false, logger);
        ric = Procedures.reconnectLogin("riccardo", false, logger);
        pit = Procedures.reconnectLogin("pietro", false, logger);

        Wizard wTom = new Wizard(2, new ArrayList<>(), Tower.BLACK, 8);
        Wizard wRic = new Wizard(1, new ArrayList<>(), Tower.WHITE, 8);
        Wizard wPit = new Wizard(0, new ArrayList<>(), Tower.GRAY, 8);

        waitOnAll(InitialState.class);
        waitOnAll(UserConnected.class);
        waitOnAll(UserConnected.class);
        waitOnAllWithCheck(UserConnected.class);

        ric.send(new CardChoice(wRic, 1));
        waitOnAllWithCheck(CardChoice.class);
        tom.send(new CardChoice(wTom, 6));
        waitOnAllWithCheck(CardChoice.class);
        pit.send(new CardChoice(wPit, 5));
        waitOnAllWithCheck(CardChoice.class);


        ric.send(new DiningRoomMovement(wRic, StudentColor.BLUE));
        waitOnAllWithCheck(DiningRoomMovement.class);
        ric.send(new DiningRoomMovement(wRic, StudentColor.BLUE));
        waitOnAllWithCheck(DiningRoomMovement.class);
        ric.send(new DiningRoomMovement(wRic, StudentColor.RED));
        waitOnAllWithCheck(DiningRoomMovement.class);
        ric.send(new DiningRoomMovement(wRic, StudentColor.RED));
        waitOnAllWithCheck(DiningRoomMovement.class);
        ric.send(new MotherNatureMovement(wRic, 1));
        waitOnAllWithCheck(MotherNatureMovement.class);
        ric.send(new CloudChoice(wRic, 0));
        waitOnAllWithCheck(CloudChoice.class);

        pit.send(new DiningRoomMovement(wPit, StudentColor.BLUE));
        waitOnAllWithCheck(DiningRoomMovement.class);
        pit.send(new DiningRoomMovement(wPit, StudentColor.BLUE));
        waitOnAllWithCheck(DiningRoomMovement.class);
        pit.send(new DiningRoomMovement(wPit, StudentColor.BLUE));
        waitOnAllWithCheck(DiningRoomMovement.class);
        pit.send(new DiningRoomMovement(wPit, StudentColor.PINK));
        waitOnAllWithCheck(DiningRoomMovement.class);
        pit.send(new MotherNatureMovement(wPit, 2));
        waitOnAllWithCheck(MotherNatureMovement.class);
        pit.send(new CloudChoice(wPit, 2));
        waitOnAllWithCheck(CloudChoice.class);

        tom.send(new IslandMovement(wTom, StudentColor.YELLOW, 4));
        waitOnAllWithCheck(IslandMovement.class);
        tom.send(new IslandMovement(wTom, StudentColor.YELLOW, 4));
        waitOnAllWithCheck(IslandMovement.class);
        tom.send(new IslandMovement(wTom, StudentColor.YELLOW, 4));
        waitOnAllWithCheck(IslandMovement.class);
        tom.send(new DiningRoomMovement(wTom, StudentColor.BLUE));
        waitOnAllWithCheck(DiningRoomMovement.class);
        tom.send(new MotherNatureMovement(wTom, 2));
        waitOnAllWithCheck(MotherNatureMovement.class);
        tom.send(new CloudChoice(wTom, 1));
        waitOnAll(CloudChoice.class);

        waitOnAll(Disconnected.class);

        TimeUnit.MILLISECONDS.sleep(100);

        assert (s.noGamesRunning());
        s.stop();
        t.join();
    }
}

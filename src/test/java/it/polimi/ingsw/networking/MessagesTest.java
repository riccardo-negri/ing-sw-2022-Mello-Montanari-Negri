package it.polimi.ingsw.networking;

import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;
import it.polimi.ingsw.networking.moves.*;
import it.polimi.ingsw.utils.LogFormatter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

class MessagesTest {

    Logger logger = LogFormatter.getLogger("Test");

    private static final String sampleState = """
            {"gameMode":"COMPLETE","playerNumber":"THREE","wizardList":[{"wizardId":0,"money":2,"cardDeck":{"assistantCards":["FIVE","SEVEN","NINE"]},"entranceStudents":["RED","RED","RED","PINK","RED","GREEN","BLUE","BLUE","BLUE"],"diningStudents":[7,5,6,0,6],"towerColor":"BLACK","towerNumber":1},{"wizardId":1,"money":6,"cardDeck":{"assistantCards":["ONE","NINE","TEN"]},"entranceStudents":["YELLOW","BLUE","YELLOW","RED","PINK","BLUE","RED","PINK","YELLOW"],"diningStudents":[3,4,7,6,7],"towerColor":"WHITE","towerNumber":3},{"wizardId":2,"money":5,"cardDeck":{"assistantCards":["SIX","EIGHT","NINE"]},"entranceStudents":["PINK","PINK","YELLOW","YELLOW","YELLOW","YELLOW","PINK","RED","BLUE"],"diningStudents":[3,6,6,7,0],"towerColor":"GRAY","towerNumber":2}],"professors":[{"color":"YELLOW","master":0},{"color":"BLUE","master":2},{"color":"GREEN","master":1},{"color":"RED","master":2},{"color":"PINK","master":1}],"islandGroupList":[{"islandGroupId":0,"islandList":[{"islandId":10,"stopCard":false,"studentColorList":["GREEN"]},{"islandId":11,"stopCard":false,"studentColorList":["GREEN"]},{"islandId":0,"stopCard":false,"studentColorList":["RED","PINK","BLUE"]}],"tower":"GRAY"},{"islandGroupId":1,"islandList":[{"islandId":1,"stopCard":false,"studentColorList":["PINK","GREEN","RED"]}],"tower":"WHITE"},{"islandGroupId":2,"islandList":[{"islandId":2,"stopCard":false,"studentColorList":["BLUE","GREEN","YELLOW","YELLOW"]}],"tower":"BLACK"},{"islandGroupId":3,"islandList":[{"islandId":3,"stopCard":false,"studentColorList":["BLUE"]}],"tower":"GRAY"},{"islandGroupId":4,"islandList":[{"islandId":4,"stopCard":false,"studentColorList":["RED"]}],"tower":"WHITE"},{"islandGroupId":8,"islandList":[{"islandId":5,"stopCard":false,"studentColorList":["YELLOW"]},{"islandId":6,"stopCard":false,"studentColorList":["YELLOW"]},{"islandId":7,"stopCard":false,"characterFive":{"stopNumber":4,"characterId":5,"activator":0,"price":2,"used":true},"studentColorList":["RED","PINK","PINK"]},{"islandId":8,"stopCard":false,"studentColorList":["YELLOW","GREEN","YELLOW"]}],"tower":"BLACK"},{"islandGroupId":9,"islandList":[{"islandId":9,"stopCard":false,"studentColorList":["PINK","RED","GREEN"]}],"tower":"WHITE"}],"cloudList":[{"cloudId":0,"playerNumber":"THREE","studentColorList":["BLUE"],"taken":false},{"cloudId":1,"playerNumber":"THREE","studentColorList":[],"taken":false},{"cloudId":2,"playerNumber":"THREE","studentColorList":[],"taken":false}],"characters":[{"studentColorList":["PINK","RED","BLUE","PINK"],"characterId":1,"activator":0,"price":1,"used":true},{"characterId":3,"activator":2,"price":3,"used":true},{"stopNumber":4,"characterId":5,"activator":0,"price":2,"used":true}],"bag":{"studentListIn":[],"studentListOut":[],"colorNumber":[0,0,0,0,0],"total":0,"empty":true},"gameState":{"gameState":"PS","playerOrder":[1,2,0],"currentlyPlaying":0},"gameEnded":false}""";

    private static final List<String> usernames = Arrays.asList("user1", "user2", "user3", "user4");

    private static final List<LobbyDescriptor> sampleLobbies = Arrays.asList(
            new LobbyDescriptor("#prv", PlayerNumber.TWO, GameMode.COMPLETE, usernames),
            new LobbyDescriptor("#asd", PlayerNumber.FOUR, GameMode.EASY, usernames),
            new LobbyDescriptor("#C3T", PlayerNumber.THREE, GameMode.COMPLETE, usernames),
            new LobbyDescriptor("#199", PlayerNumber.TWO, GameMode.EASY, usernames)
    );

    @Test
    void startTest(){
        List<Connection> connections = Procedures.twoBoundedConnections(logger);
        Connection c1 = connections.get(0);
        Connection c2 = connections.get(1);
        c1.bindFunction(this::processSet1);
        c2.bindFunction(this::processSet2);

        sendAllMessages(c1);
        waitSet1(c2);

        sendAllMessages(c2);
        waitSet2(c1);
    }

    boolean processSet1(Connection connection) {
        Message m = connection.getLastMessage();
        if (m instanceof InitialState is) {
            assert (is.getState().equals(sampleState) && is.getUsernames().equals(usernames));
            return true;
        } else if (m instanceof Login l) {
            assert (l.username().equals("loggedGuy"));
            return true;
        } else if (m instanceof Redirect r) {
            assert (r.port() == 41124);
            return true;
        } else if (m instanceof CreateLobby cl) {
            assert (cl.getPlayerNumber().equals(PlayerNumber.THREE) && cl.getGameMode().equals(GameMode.EASY));
            return true;
        } else if (m instanceof UserConnected uc) {
            assert (uc.username().equals("connectedGuy"));
            return true;
        } else if (m instanceof CardChoice cc) {
            assert (cc.getAuthorId() == 1 && cc.getCard() == 4);
            return true;
        } else if (m instanceof DiningRoomMovement drm) {
            assert (drm.getAuthorId() == 1 && drm.getStudent().equals(StudentColor.BLUE));
            return true;
        } else if (m instanceof MotherNatureMovement mnm) {
            assert (mnm.getAuthorId() == 1 && mnm.getSteps() == 5);
            return true;
        }
        return false;
    }

    boolean processSet2(Connection connection) {
        Message m = connection.getLastMessage();
        if (m instanceof ErrorMessage em) {
            return true;
        } else if (m instanceof LobbiesList ll) {
            assert (ll.lobbies.equals(sampleLobbies));
            return true;
        } else if (m instanceof UserResigned ur) {
            assert (ur.getUsername().equals("resignedGuy"));
            return true;
        } else if (m instanceof LobbyChoice lc) {
            assert (lc.code().equals("#t3S"));
            return true;
        } else if (m instanceof UserDisconnected ud) {
            assert (ud.username().equals("disconnectedGuy"));
            return true;
        } else if (m instanceof IslandMovement im) {
            assert (im.getAuthorId() == 1 && im.getStudent().equals(StudentColor.GREEN) && im.getIslandId() == 9);
            return true;
        } else if (m instanceof CloudChoice cc) {
            assert (cc.getAuthorId() == 1 && cc.getCloudId() == 2);
            return true;
        }
        return false;
    }

    void sendAllMessages(Connection sender) {
        List<Message> allMessages = new ArrayList<>();
        Wizard w = new Wizard(1, new ArrayList<>(), Tower.BLACK, 8);

        allMessages.add(new CreateLobby(PlayerNumber.THREE, GameMode.EASY));
        allMessages.add(new ErrorMessage());
        allMessages.add(new InitialState(sampleState, usernames));
        allMessages.add(new LobbiesList(sampleLobbies));
        allMessages.add(new LobbyChoice("#t3S"));
        allMessages.add(new Login("loggedGuy"));
        allMessages.add(new Ping());
        allMessages.add(new Redirect(41124));
        allMessages.add(new UserConnected("connectedGuy"));
        allMessages.add(new UserDisconnected("disconnectedGuy"));
        allMessages.add(new UserResigned("resignedGuy"));
        allMessages.add(new CardChoice(w, 4));
        allMessages.add(new CloudChoice(w, 2));
        allMessages.add(new DiningRoomMovement(w, StudentColor.BLUE));
        allMessages.add(new IslandMovement(w, StudentColor.GREEN, 9));
        allMessages.add(new MotherNatureMovement(w, 5));

        for (Message m: allMessages) {
            sender.send(m);
        }
    }

    void waitSet1(Connection receiver) {
        UserConnected uc = (UserConnected) receiver.waitMessage(UserConnected.class);
        assert (uc.username().equals("connectedGuy"));
        InitialState is = (InitialState) receiver.waitMessage(InitialState.class);
        assert (is.getState().equals(sampleState) && is.getUsernames().equals(usernames));
        CreateLobby cl = (CreateLobby) receiver.waitMessage(CreateLobby.class);
        assert (cl.getPlayerNumber().equals(PlayerNumber.THREE) && cl.getGameMode().equals(GameMode.EASY));
        Login l = (Login) receiver.waitMessage(Login.class);
        assert (l.username().equals("loggedGuy"));
        CardChoice cc = (CardChoice) receiver.waitMessage(CardChoice.class);
        assert (cc.getAuthorId() == 1 && cc.getCard() == 4);
        DiningRoomMovement drm = (DiningRoomMovement) receiver.waitMessage(DiningRoomMovement.class);
        assert (drm.getAuthorId() == 1 && drm.getStudent().equals(StudentColor.BLUE));
        MotherNatureMovement mnm = (MotherNatureMovement) receiver.waitMessage(MotherNatureMovement.class);
        assert (mnm.getAuthorId() == 1 && mnm.getSteps() == 5);
        Redirect r = (Redirect) receiver.waitMessage();
        assert (r.port() == 41124);
        assert receiver.noMessageLeft();
    }

    void waitSet2(Connection receiver) {
        LobbiesList ll = (LobbiesList) receiver.waitMessage(LobbiesList.class);
        assert (ll.lobbies.equals(sampleLobbies));
        LobbyChoice lc = (LobbyChoice) receiver.waitMessage(LobbyChoice.class);
        assert (lc.code().equals("#t3S"));
        ErrorMessage em = (ErrorMessage) receiver.waitMessage(ErrorMessage.class);
        UserResigned ur = (UserResigned) receiver.waitMessage(UserResigned.class);
        assert (ur.getUsername().equals("resignedGuy"));
        IslandMovement im = (IslandMovement) receiver.waitMessage(IslandMovement.class);
        assert (im.getAuthorId() == 1 && im.getStudent().equals(StudentColor.GREEN) && im.getIslandId() == 9);
        CloudChoice cc = (CloudChoice) receiver.waitMessage(CloudChoice.class);
        assert (cc.getAuthorId() == 1 && cc.getCloudId() == 2);
        UserDisconnected ud = (UserDisconnected) receiver.waitMessage();
        assert (ud.username().equals("disconnectedGuy"));
        assert receiver.noMessageLeft();
    }
}

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

    private static final List<StudentColor> studentList1 = Arrays.asList(StudentColor.PINK, StudentColor.GREEN, StudentColor.YELLOW);
    private static final List<StudentColor> studentList2 = Arrays.asList(StudentColor.BLUE, StudentColor.RED, StudentColor.RED);

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
        Message m = connection.getFirstMessage();
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
        } else if (m instanceof UseCharacter1 uc1) {
            assert (uc1.getAuthorId() == 1 && uc1.getStudent() == StudentColor.RED && uc1.getIslandId() == 5);
            return true;
        } else if (m instanceof UseCharacter3 uc3) {
            assert (uc3.getAuthorId() == 1 && uc3.getIslandGroupId() == 2);
            return true;
        } else if (m instanceof UseCharacter5 uc5) {
            assert (uc5.getAuthorId() == 1 && uc5.getIslandId() == 6);
            return true;
        } else if (m instanceof UseCharacter7 uc7) {
            assert (uc7.getAuthorId() == 1 && uc7.getTaken().equals(studentList1) && uc7.getGiven().equals(studentList2));
            return true;
        } else if (m instanceof UseCharacter9 uc9) {
            assert (uc9.getAuthorId() == 1 && uc9.getColor() == StudentColor.GREEN);
            return true;
        } else if (m instanceof UseCharacter11 uc11) {
            assert (uc11.getAuthorId() == 1 && uc11.getStudent() == StudentColor.YELLOW);
            return true;
        }
        return false;
    }

    boolean processSet2(Connection connection) {
        Message m = connection.getFirstMessage();
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
        } else if (m instanceof UseCharacter2 uc2) {
            assert (uc2.getAuthorId() == 1);
            return true;
        } else if (m instanceof UseCharacter4 uc4) {
            assert (uc4.getAuthorId() == 1);
            return true;
        } else if (m instanceof UseCharacter6 uc6) {
            assert (uc6.getAuthorId() == 1);
            return true;
        } else if (m instanceof UseCharacter8 uc8) {
            assert (uc8.getAuthorId() == 1);
            return true;
        } else if (m instanceof UseCharacter10 uc10) {
            assert (uc10.getAuthorId() == 1 && uc10.getTaken().equals(studentList2) && uc10.getGiven().equals(studentList1));
            return true;
        } else if (m instanceof UseCharacter12 uc12) {
            assert (uc12.getAuthorId() == 1 && uc12.getColor() == StudentColor.BLUE);
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
        allMessages.add(new UseCharacter1(w, StudentColor.RED, 5));
        allMessages.add(new UseCharacter2(w));
        allMessages.add(new UseCharacter3(w, 2));
        allMessages.add(new UseCharacter4(w));
        allMessages.add(new UseCharacter5(w, 6));
        allMessages.add(new UseCharacter6(w));
        allMessages.add(new UseCharacter7(w,  studentList1, studentList2));
        allMessages.add(new UseCharacter8(w));
        allMessages.add(new UseCharacter9(w, StudentColor.GREEN));
        allMessages.add(new UseCharacter10(w, studentList2, studentList1));
        allMessages.add(new UseCharacter11(w, StudentColor.YELLOW));
        allMessages.add(new UseCharacter12(w, StudentColor. BLUE));

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
        UseCharacter1 uc1 = (UseCharacter1) receiver.waitMessage(UseCharacter1.class);
        assert (uc1.getAuthorId() == 1 && uc1.getStudent() == StudentColor.RED && uc1.getIslandId() == 5);
        UseCharacter3 uc3 = (UseCharacter3) receiver.waitMessage(UseCharacter3.class);
        assert (uc3.getAuthorId() == 1 && uc3.getIslandGroupId() == 2);
        UseCharacter5 uc5 = (UseCharacter5) receiver.waitMessage(UseCharacter5.class);
        assert (uc5.getAuthorId() == 1 && uc5.getIslandId() == 6);
        UseCharacter7 uc7 = (UseCharacter7) receiver.waitMessage(UseCharacter7.class);
        assert (uc7.getAuthorId() == 1 && uc7.getTaken().equals(studentList1) && uc7.getGiven().equals(studentList2));
        UseCharacter9 uc9 = (UseCharacter9) receiver.waitMessage(UseCharacter9.class);
        assert (uc9.getAuthorId() == 1 && uc9.getColor() == StudentColor.GREEN);
        UseCharacter11 uc11 = (UseCharacter11) receiver.waitMessage(UseCharacter11.class);
        assert (uc11.getAuthorId() == 1 && uc11.getStudent() == StudentColor.YELLOW);
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
        UseCharacter2 uc2 = (UseCharacter2) receiver.waitMessage(UseCharacter2.class);
        assert (uc2.getAuthorId() == 1);
        UseCharacter4 uc4 = (UseCharacter4) receiver.waitMessage(UseCharacter4.class);
        assert (uc4.getAuthorId() == 1);
        UseCharacter6 uc6 = (UseCharacter6) receiver.waitMessage(UseCharacter6.class);
        assert (uc6.getAuthorId() == 1);
        UseCharacter8 uc8 = (UseCharacter8) receiver.waitMessage(UseCharacter8.class);
        assert (uc8.getAuthorId() == 1);
        UseCharacter10 uc10 = (UseCharacter10) receiver.waitMessage(UseCharacter10.class);
        assert (uc10.getAuthorId() == 1 && uc10.getTaken().equals(studentList2) && uc10.getGiven().equals(studentList1));
        UseCharacter12 uc12 = (UseCharacter12) receiver.waitMessage(UseCharacter12.class);
        assert (uc12.getAuthorId() == 1 && uc12.getColor() == StudentColor.BLUE);
        UserDisconnected ud = (UserDisconnected) receiver.waitMessage();
        assert (ud.username().equals("disconnectedGuy"));
        assert receiver.noMessageLeft();
    }
}

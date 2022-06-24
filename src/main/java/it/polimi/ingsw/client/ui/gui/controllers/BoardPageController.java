package it.polimi.ingsw.client.ui.gui.controllers;

import com.sun.scenario.effect.Blend;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.BoardPageGUI;
import it.polimi.ingsw.client.ui.gui.records.*;
import it.polimi.ingsw.client.ui.gui.records.CharacterRecord;
import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.gameState.GameState;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.moves.Move;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static it.polimi.ingsw.client.ui.gui.utils.BoardUtils.*;

public class BoardPageController extends AbstractController {

    @FXML
    GridPane island0;
    @FXML
    GridPane island1;
    @FXML
    GridPane island2;
    @FXML
    GridPane island3;
    @FXML
    GridPane island4;
    @FXML
    GridPane island5;
    @FXML
    GridPane island6;
    @FXML
    GridPane island7;
    @FXML
    GridPane island8;
    @FXML
    GridPane island9;
    @FXML
    GridPane island10;
    @FXML
    GridPane island11;

    @FXML
    ImageView cloud0;
    @FXML
    ImageView cloud1;
    @FXML
    ImageView cloud2;
    @FXML
    ImageView cloud3;
    @FXML
    ImageView student0Cloud0;
    @FXML
    ImageView student0Cloud1;
    @FXML
    ImageView student0Cloud2;
    @FXML
    ImageView student0Cloud3;
    @FXML
    ImageView student1Cloud0;
    @FXML
    ImageView student1Cloud1;
    @FXML
    ImageView student1Cloud2;
    @FXML
    ImageView student1Cloud3;
    @FXML
    ImageView student2Cloud0;
    @FXML
    ImageView student2Cloud1;
    @FXML
    ImageView student2Cloud2;
    @FXML
    ImageView student2Cloud3;
    @FXML
    ImageView student3Cloud0;
    @FXML
    ImageView student3Cloud1;
    @FXML
    ImageView student3Cloud2;
    @FXML
    ImageView student3Cloud3;

    @FXML
    ImageView myStudent0;
    @FXML
    ImageView myStudent1;
    @FXML
    ImageView myStudent2;
    @FXML
    ImageView myStudent3;
    @FXML
    ImageView myStudent4;
    @FXML
    ImageView myStudent5;
    @FXML
    ImageView myStudent6;
    @FXML
    ImageView myStudent7;
    @FXML
    ImageView myStudent8;

    @FXML
    FlowPane myGreenTable;
    @FXML
    FlowPane myRedTable;
    @FXML
    FlowPane myYellowTable;
    @FXML
    FlowPane myBlueTable;
    @FXML
    FlowPane myFuchsiaTable;

    @FXML
    ImageView myGreenProfessor;
    @FXML
    ImageView myRedProfessor;
    @FXML
    ImageView myYellowProfessor;
    @FXML
    ImageView myBlueProfessor;
    @FXML
    ImageView myFuchsiaProfessor;

    @FXML
    ImageView myTower0;
    @FXML
    ImageView myTower1;
    @FXML
    ImageView myTower2;
    @FXML
    ImageView myTower3;
    @FXML
    ImageView myTower4;
    @FXML
    ImageView myTower5;
    @FXML
    ImageView myTower6;
    @FXML
    ImageView myTower7;

    @FXML
    ImageView otherStudent0;
    @FXML
    ImageView otherStudent1;
    @FXML
    ImageView otherStudent2;
    @FXML
    ImageView otherStudent3;
    @FXML
    ImageView otherStudent4;
    @FXML
    ImageView otherStudent5;
    @FXML
    ImageView otherStudent6;
    @FXML
    ImageView otherStudent7;
    @FXML
    ImageView otherStudent8;

    @FXML
    FlowPane otherGreenTable;
    @FXML
    FlowPane otherRedTable;
    @FXML
    FlowPane otherYellowTable;
    @FXML
    FlowPane otherBlueTable;
    @FXML
    FlowPane otherFuchsiaTable;

    @FXML
    ImageView otherGreenProfessor;
    @FXML
    ImageView otherRedProfessor;
    @FXML
    ImageView otherYellowProfessor;
    @FXML
    ImageView otherBlueProfessor;
    @FXML
    ImageView otherFuchsiaProfessor;

    @FXML
    ImageView otherTower0;
    @FXML
    ImageView otherTower1;
    @FXML
    ImageView otherTower2;
    @FXML
    ImageView otherTower3;
    @FXML
    ImageView otherTower4;
    @FXML
    ImageView otherTower5;
    @FXML
    ImageView otherTower6;
    @FXML
    ImageView otherTower7;

    @FXML
    Label username0;
    @FXML
    Label username1;
    @FXML
    Label username2;
    @FXML
    Label username3;

    @FXML
    ImageView wizard0;
    @FXML
    ImageView wizard1;
    @FXML
    ImageView wizard2;
    @FXML
    ImageView wizard3;

    @FXML
    ImageView assistant0;
    @FXML
    ImageView assistant1;
    @FXML
    ImageView assistant2;
    @FXML
    ImageView assistant3;

    @FXML
    ImageView hourglass0;
    @FXML
    ImageView hourglass1;
    @FXML
    ImageView hourglass2;
    @FXML
    ImageView hourglass3;

    @FXML
    Label coinNumber0;
    @FXML
    Label coinNumber1;
    @FXML
    Label coinNumber2;
    @FXML
    Label coinNumber3;

    @FXML
    ImageView coinImage0;
    @FXML
    ImageView coinImage1;
    @FXML
    ImageView coinImage2;
    @FXML
    ImageView coinImage3;

    @FXML
    ImageView disconnected1;
    @FXML
    ImageView disconnected2;
    @FXML
    ImageView disconnected3;

    @FXML
    ImageView arrow1;
    @FXML
    ImageView arrow2;
    @FXML
    ImageView arrow3;

    @FXML
    Label priceCharacter0;
    @FXML
    Label priceCharacter1;
    @FXML
    Label priceCharacter2;

    @FXML
    ImageView item0Character0;
    @FXML
    ImageView item1Character0;
    @FXML
    ImageView item2Character0;
    @FXML
    ImageView item3Character0;
    @FXML
    ImageView item4Character0;
    @FXML
    ImageView item5Character0;
    @FXML
    ImageView item0Character1;
    @FXML
    ImageView item1Character1;
    @FXML
    ImageView item2Character1;
    @FXML
    ImageView item3Character1;
    @FXML
    ImageView item4Character1;
    @FXML
    ImageView item5Character1;
    @FXML
    ImageView item0Character2;
    @FXML
    ImageView item1Character2;
    @FXML
    ImageView item2Character2;
    @FXML
    ImageView item3Character2;
    @FXML
    ImageView item4Character2;
    @FXML
    ImageView item5Character2;

    @FXML
    ImageView card1;
    @FXML
    ImageView card2;
    @FXML
    ImageView card3;
    @FXML
    ImageView card4;
    @FXML
    ImageView card5;
    @FXML
    ImageView card6;
    @FXML
    ImageView card7;
    @FXML
    ImageView card8;
    @FXML
    ImageView card9;
    @FXML
    ImageView card10;

    @FXML
    Label roundNumber;
    @FXML
    Label turnPhaseCode;

    BoardRecord board;

    Integer selectedOtherUser = null;

    @FXML
    private void handleResign (ActionEvent event) {
        client.getConnection().send(new UserResigned(client.getUsername()));
    }

    @FXML
    private void handleAssistantCard1 (Event event) { handleAssistantCard(1); }
    @FXML
    private void handleAssistantCard2 (Event event) { handleAssistantCard(2); }
    @FXML
    private void handleAssistantCard3 (Event event) { handleAssistantCard(3); }
    @FXML
    private void handleAssistantCard4 (Event event) { handleAssistantCard(4); }
    @FXML
    private void handleAssistantCard5 (Event event) { handleAssistantCard(5); }
    @FXML
    private void handleAssistantCard6 (Event event) { handleAssistantCard(6); }
    @FXML
    private void handleAssistantCard7 (Event event) { handleAssistantCard(7); }
    @FXML
    private void handleAssistantCard8 (Event event) { handleAssistantCard(8); }
    @FXML
    private void handleAssistantCard9 (Event event) { handleAssistantCard(9); }
    @FXML
    private void handleAssistantCard10 (Event event) { handleAssistantCard(10); }

    @FXML
    private void handleUserChange1() {  handleUserChange(1); }

    @FXML
    private void handleUserChange2() {  handleUserChange(2); }

    @FXML
    private void handleUserChange3() {  handleUserChange(3); }

    private void handleUserChange (int userId) {
        List<String> usersNotMyUser = new ArrayList<>(client.getUsernames());
        usersNotMyUser.remove(client.getUsername());
        if (selectedOtherUser != null && userId == selectedOtherUser) {
            // is a deselection
            removeHighlight(board.users().get(userId).wizard());
            // the board to show is the one from the playing player
            int currentWizardIndex = client.getModel().getGameState().getCurrentPlayer();
            String currentUsername = client.getUsernames().get(currentWizardIndex);
            int currentUserArea = usersNotMyUser.indexOf(currentUsername);
            if (currentUserArea != -1)
                userId = currentUserArea + 1;
            selectedOtherUser = null;
        }
        else {
            // is a new selection
            if (selectedOtherUser != null)
                removeHighlight(board.users().get(selectedOtherUser).wizard());
            highlight(board.users().get(userId).wizard());
            selectedOtherUser = userId;
        }
        int toShow = userId;
        Platform.runLater(() -> updateSchoolBoard(board, client, usersNotMyUser.get(toShow - 1)));
    }

    @FXML
    private void handleStudentPick0(Event event) { handleStudentPick(0); }
    @FXML
    private void handleStudentPick1(Event event) { handleStudentPick(1); }
    @FXML
    private void handleStudentPick2(Event event) { handleStudentPick(2); }
    @FXML
    private void handleStudentPick3(Event event) { handleStudentPick(3); }
    @FXML
    private void handleStudentPick4(Event event) { handleStudentPick(4); }
    @FXML
    private void handleStudentPick5(Event event) { handleStudentPick(5); }
    @FXML
    private void handleStudentPick6(Event event) { handleStudentPick(6); }
    @FXML
    private void handleStudentPick7(Event event) { handleStudentPick(7); }
    @FXML
    private void handleStudentPick8(Event event) { handleStudentPick(8); }

    @FXML
    private void handleDiningRed(Event event) { handleDiningTable(StudentColor.RED); }
    @FXML
    private void handleDiningGreen(Event event) { handleDiningTable(StudentColor.GREEN); }
    @FXML
    private void handleDiningBlue(Event event) { handleDiningTable(StudentColor.BLUE); }
    @FXML
    private void handleDiningPink(Event event) { handleDiningTable(StudentColor.PINK); }
    @FXML
    private void handleDiningYellow(Event event) { handleDiningTable(StudentColor.YELLOW); }

    @FXML
    private void handleIsland0(Event event) { handleIsland(0); }
    @FXML
    private void handleIsland1(Event event) { handleIsland(1); }
    @FXML
    private void handleIsland2(Event event) { handleIsland(2); }
    @FXML
    private void handleIsland3(Event event) { handleIsland(3); }
    @FXML
    private void handleIsland4(Event event) { handleIsland(4); }
    @FXML
    private void handleIsland5(Event event) { handleIsland(5); }
    @FXML
    private void handleIsland6(Event event) { handleIsland(6); }
    @FXML
    private void handleIsland7(Event event) { handleIsland(7); }
    @FXML
    private void handleIsland8(Event event) { handleIsland(8); }
    @FXML
    private void handleIsland9(Event event) { handleIsland(9); }
    @FXML
    private void handleIsland10(Event event) { handleIsland(10); }
    @FXML
    private void handleIsland11(Event event) { handleIsland(11); }

    @FXML
    private void handleCloud0(Event event) { handleCloud(0); }
    @FXML
    private void handleCloud1(Event event) { handleCloud(1); }
    @FXML
    private void handleCloud2(Event event) { handleCloud(2); }
    @FXML
    private void handleCloud3(Event event) { handleCloud(3); }

    @FXML
    private void handleCharacter0(Event event) { handleCharacter(0); }
    @FXML
    private void handleCharacter1(Event event) { handleCharacter(1); }
    @FXML
    private void handleCharacter2(Event event) { handleCharacter(2); }

    @FXML
    private void handleItem0Character0 (Event event) { handleCharacterItem(0,0); }
    @FXML
    private void handleItem1Character0 (Event event) { handleCharacterItem(0,1); }
    @FXML
    private void handleItem2Character0 (Event event) { handleCharacterItem(0,2); }
    @FXML
    private void handleItem3Character0 (Event event) { handleCharacterItem(0,3); }
    @FXML
    private void handleItem4Character0 (Event event) { handleCharacterItem(0,4); }
    @FXML
    private void handleItem5Character0 (Event event) { handleCharacterItem(0,5); }

    @FXML
    private void handleItem0Character1 (Event event) { handleCharacterItem(1,0); }
    @FXML
    private void handleItem1Character1 (Event event) { handleCharacterItem(1,1); }
    @FXML
    private void handleItem2Character1 (Event event) { handleCharacterItem(1,2); }
    @FXML
    private void handleItem3Character1 (Event event) { handleCharacterItem(1,3); }
    @FXML
    private void handleItem4Character1 (Event event) { handleCharacterItem(1,4); }
    @FXML
    private void handleItem5Character1 (Event event) { handleCharacterItem(1,5); }

    @FXML
    private void handleItem0Character2 (Event event) { handleCharacterItem(2,0); }
    @FXML
    private void handleItem1Character2 (Event event) { handleCharacterItem(2,1); }
    @FXML
    private void handleItem2Character2 (Event event) { handleCharacterItem(2,2); }
    @FXML
    private void handleItem3Character2 (Event event) { handleCharacterItem(2,3); }
    @FXML
    private void handleItem4Character2 (Event event) { handleCharacterItem(2,4); }
    @FXML
    private void handleItem5Character2 (Event event) { handleCharacterItem(2,5); }


    public void handleStudentPick (int number) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        switch (model.getGameState().getGameStateName()) {
            case "MSS":
                removeHighlight(board.myBoard().entrance());
                if (gui.getStudentPicked() == number) gui.setStudentPicked(-1);
                else {
                    gui.setStudentPicked(number);
                    highlight(board.myBoard().entrance().get(number));
                }
                break;
        }
    }

    public void handleAssistantCard(int card) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        switch (model.getGameState().getGameStateName()) {
            case "PS":
                try {
                    gui.doCardChoice(card);
                } catch (Exception e) {
                    client.getLogger().log(Level.INFO, e.getMessage());
                } break;
        }

    }

    public void handleDiningTable(StudentColor color) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        switch (model.getGameState().getGameStateName()) {
            case "MSS":
                if (gui.getStudentPicked() == -1 || model.getWizard(client.getUsernames().indexOf(client.getUsername())).getEntranceStudents().get(gui.getStudentPicked()) != color) {
                    gui.setStudentPicked(-1);
                    removeHighlight(board.myBoard().entrance());
                } else {
                    try {
                        gui.doStudentMovement(color, "dining-room");
                    } catch (Exception e) {
                        client.getLogger().log(Level.INFO, e.getMessage());
                    }
                    gui.setStudentPicked(-1);
                    removeHighlight(board.myBoard().entrance());
                } break;
        }
    }

    public void handleIsland(int islandId) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        switch (model.getGameState().getGameStateName()) {
            case "MSS":
                if (gui.getStudentPicked() == -1)
                    client.getLogger().log(Level.INFO, "No student selected in entrance");
                else {
                    try {
                        gui.doStudentMovement(model.getWizard(client.getUsernames().indexOf(client.getUsername())).getEntranceStudents().get(gui.getStudentPicked()), "island-" + islandId);
                    } catch (Exception e) {
                        client.getLogger().log(Level.INFO, e.getMessage());
                    }
                    gui.setStudentPicked(-1);
                    removeHighlight(board.myBoard().entrance());
                } break;
            case "MMNS":
                try { gui.doMotherNatureMovement(model.getIslandGroupList().indexOf(
                        model.getIslandGroupList().stream().filter(islandGroup ->
                                islandGroup.getIslandList().contains(model.getIsland(islandId))).toList().get(0)));
                } catch (Exception e) {
                    client.getLogger().log(Level.INFO, e.getMessage());
                }
        }
    }

    public void handleCharacter(int characterNumber) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        if (Objects.equals(model.getGameState().getGameStateName(), "MSS") || Objects.equals(model.getGameState().getGameStateName(), "MMNS") || Objects.equals(model.getGameState().getGameStateName(), "CCS")) {
            List<Objects> parameters;
            switch (model.getCharacters()[characterNumber].getId()) {
                case 1:

            }
        }
    }

    public void handleCharacterItem(int character, int item) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        if (Objects.equals(model.getGameState().getGameStateName(), "MSS") || Objects.equals(model.getGameState().getGameStateName(), "MMNS") || Objects.equals(model.getGameState().getGameStateName(), "CCS")) {
            if (gui.getCharacterStudentPicked(character) == -1) {
                gui.setCharacterStudentPicked(character, item);

                highlight(board.characters().get(character).items().get(item));
            }
        }
    }

    public void undoAllSelections() {
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        board.characters().stream()
                .flatMap(characterRecord -> characterRecord.items().stream())
                .forEach(this::removeHighlight);
        board.myBoard().entrance().forEach(this::removeHighlight);

        for (int i=0; i<3; i++)
            gui.setCharacterStudentPicked(i,-1);
        gui.setStudentPicked(-1);
    }

    public void handleCloud(int cloudId) {
        Game model = client.getModel();
        BoardPageGUI gui = (BoardPageGUI) client.getCurrState();

        switch (model.getGameState().getGameStateName()) {
            case "CCS":
                try {
                    gui.doCloudChoice(cloudId);
                } catch (Exception e) {
                    client.getLogger().log(Level.INFO, e.getMessage());
                } break;
        }
    }
    @FXML
    void initialize () {
        client.getConnection().bindFunctionAndTestPrevious(this::onNewMessage);

        List<GridPane> islandsNodes = Arrays.asList(island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11);
        List<IslandRecord> islandRecords = new ArrayList<>();

        for (GridPane island : islandsNodes) {
            List<Node> nodes = island.getChildren();
            IslandRecord islandRecord = new IslandRecord((ImageView) getElementFromNodesAndIdSubstring(nodes, "tower"), (ImageView) getElementFromNodesAndIdSubstring(nodes, "motherNature"), (ImageView) getElementFromNodesAndIdSubstring(nodes, "noEntry"), Arrays.asList((ImageView) getElementFromNodesAndIdSubstring(nodes, "yellowPawn"), (ImageView) getElementFromNodesAndIdSubstring(nodes, "bluePawn"), (ImageView) getElementFromNodesAndIdSubstring(nodes, "greenPawn"), (ImageView) getElementFromNodesAndIdSubstring(nodes, "redPawn"), (ImageView) getElementFromNodesAndIdSubstring(nodes, "fuchsiaPawn")), Arrays.asList((Label) getElementFromNodesAndIdSubstring(nodes, "yellowCount"), (Label) getElementFromNodesAndIdSubstring(nodes, "blueCount"), (Label) getElementFromNodesAndIdSubstring(nodes, "greenCount"), (Label) getElementFromNodesAndIdSubstring(nodes, "redCount"), (Label) getElementFromNodesAndIdSubstring(nodes, "fuchsiaCount")));
            islandRecords.add(islandRecord);
        }

        List<CloudRecord> cloudRecords = Arrays.asList(new CloudRecord(cloud0, Arrays.asList(student0Cloud0, student1Cloud0, student2Cloud0, student3Cloud0)), new CloudRecord(cloud1, Arrays.asList(student0Cloud1, student1Cloud1, student2Cloud1, student3Cloud1)), new CloudRecord(cloud2, Arrays.asList(student0Cloud2, student1Cloud2, student2Cloud2, student3Cloud2)), new CloudRecord(cloud3, Arrays.asList(student0Cloud3, student1Cloud3, student2Cloud3, student3Cloud3)));

        SchoolBoardRecord myBoard = new SchoolBoardRecord(Arrays.asList(myStudent0, myStudent1, myStudent2, myStudent3, myStudent4, myStudent5, myStudent6, myStudent7, myStudent8), Arrays.asList(myYellowTable, myBlueTable, myGreenTable, myRedTable, myFuchsiaTable), Arrays.asList(myYellowProfessor, myBlueProfessor, myGreenProfessor, myRedProfessor, myFuchsiaProfessor), Arrays.asList(myTower0, myTower1, myTower2, myTower3, myTower4, myTower5, myTower6, myTower7));

        SchoolBoardRecord otherBoard = new SchoolBoardRecord(Arrays.asList(otherStudent0, otherStudent1, otherStudent2, otherStudent3, otherStudent4, otherStudent5, otherStudent6, otherStudent7, otherStudent8), Arrays.asList(otherYellowTable, otherBlueTable, otherGreenTable, otherRedTable, otherFuchsiaTable), Arrays.asList(otherYellowProfessor, otherBlueProfessor, otherGreenProfessor, otherRedProfessor, otherFuchsiaProfessor), Arrays.asList(otherTower0, otherTower1, otherTower2, otherTower3, otherTower4, otherTower5, otherTower6, otherTower7));

        List<UserRecord> users = Arrays.asList(new UserRecord(username0, wizard0, assistant0, null, hourglass0, coinImage0, coinNumber0), new UserRecord(username1, wizard1, assistant1, disconnected1, hourglass1, coinImage1, coinNumber1), new UserRecord(username2, wizard2, assistant2, disconnected2, hourglass2, coinImage2, coinNumber2), new UserRecord(username3, wizard3, assistant3, disconnected3, hourglass3, coinImage3, coinNumber3));

        List<CharacterRecord> characters = Arrays.asList(new CharacterRecord(null, null, priceCharacter0, Arrays.asList(item0Character0, item1Character0, item2Character0, item3Character0, item4Character0, item5Character0)), new CharacterRecord(null, null, priceCharacter1, Arrays.asList(item0Character1, item1Character1, item2Character1, item3Character1, item4Character1, item5Character1)), new CharacterRecord(null, null, priceCharacter2, Arrays.asList(item0Character2, item1Character2, item2Character2, item3Character2, item4Character2, item5Character2)));

        List<ImageView> myDeck = Arrays.asList(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10);

        List<ImageView> arrows = Arrays.asList(arrow1, arrow2, arrow3);

        board = new BoardRecord(islandRecords, cloudRecords, myBoard, otherBoard, users, characters, myDeck, arrows, roundNumber, turnPhaseCode);

        updateBoard(board, client, selectedOtherUser);
    }

    boolean onNewMessage (Connection source) {
        Message m = source.getFirstMessage();
        if (m instanceof UserResigned ur) {
            client.setResigned(ur.getUsername());
            ((AbstractBoardPage) client.getCurrState()).onEnd(false);
            client.getConnection().close();
            Platform.runLater(() -> client.drawNextPage());
            return true;
        }
        else if (m instanceof Move move) {
            try {
                move.applyEffectClient(client.getModel());
            } catch (Exception e) {
                String toLog = "Received an invalid move: " + e.getMessage();
                client.getLogger().log(Level.WARNING, toLog);
            }
            Platform.runLater(() -> updateBoard(board, client, selectedOtherUser));
        } else if (m instanceof Disconnected) {
            ((AbstractBoardPage) client.getCurrState()).onEnd(true);
            client.getConnection().close();
            client.setJustDisconnected(true);
            Platform.runLater(() -> client.drawNextPage());
        } else if (m instanceof UserDisconnected userDisconnected) {
            client.getUsernamesDisconnected().add(userDisconnected.username());
            Platform.runLater(() -> updateBoard(board, client, selectedOtherUser));
        } else if (m instanceof UserConnected userConnected) {
            client.getUsernamesDisconnected().remove(userConnected.username());
            Platform.runLater(() -> updateBoard(board, client, selectedOtherUser));
        }
        return false;
    }

    void highlight(ImageView image) {
        image.setStyle("-fx-effect : dropshadow(gaussian, yellow, 4, 1, 0, 0);");
    }

    void highlight(List<ImageView> images) {
        for (ImageView i: images)
            highlight(i);
    }

    void removeHighlight(ImageView image) {
        image.setStyle("");
    }

    void removeHighlight(List<ImageView> images) {
        for (ImageView i: images)
            removeHighlight(i);
    }

}
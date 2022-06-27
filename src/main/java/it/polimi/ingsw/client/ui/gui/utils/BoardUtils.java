package it.polimi.ingsw.client.ui.gui.utils;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.GUI;
import it.polimi.ingsw.client.ui.gui.records.*;
import it.polimi.ingsw.model.entity.*;
import it.polimi.ingsw.model.entity.characters.*;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.gameState.ActionState;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class BoardUtils {
    private BoardUtils () {
    }

    /**
     * retrieve the round number
     * @param model model of the game
     * @return the round number
     */
    private static int findRoundNumber (Game model) {
        if (model.getGameState().getGameStateName().equals("PS")) {
            return 11 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
        }
        return 10 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
    }

    /**
     * helper method to get an element from a list given the id (if present)
     * @param nodes list of nodes
     * @param idSubstring id to compare
     * @return node if present, null otherwise
     */
    public static Object getElementFromNodesAndIdSubstring (List<Node> nodes, String idSubstring) {
        for (Node node : nodes) {
            String id = node.getId();
            if (id != null && id.contains(idSubstring)) {
                return node;
            }
        }
        return null;
    }

    /**
     * asset retrieve for towers
     * @param tower type to get image for
     * @return the Image object
     */
    private static Image getTowerImageFromColor (Tower tower) {
        return switch (tower) {
            case WHITE -> new Image("assets/pawns/towers/board/white.png");
            case BLACK -> new Image("assets/pawns/towers/board/black.png");
            case GRAY -> new Image("assets/pawns/towers/board/gray.png");
        };
    }

    /**
     * asset retrieve for students
     * @param color type to get image for
     * @return the Image object
     */
    private static Image getStudentImageFromColor (StudentColor color) {
        return switch (color) {
            case RED -> new Image("assets/pawns/students/3d/red.png");
            case BLUE -> new Image("assets/pawns/students/3d/blue.png");
            case PINK -> new Image("assets/pawns/students/3d/fuchsia.png");
            case GREEN -> new Image("assets/pawns/students/3d/green.png");
            case YELLOW -> new Image("assets/pawns/students/3d/yellow.png");
        };
    }

    /**
     * asset retrieve for wizards
     * @param id type to get image for
     * @return the Image object
     */
    private static Image getWizardImageFromId (int id) {
        return new Image("assets/cards/wizards/" + id + ".png");
    }

    /**
     * asset retrieve for assistants
     * @param id type to get image for
     * @return the Image object
     */
    private static Image getAssistantImageFromId (int id) {
        return new Image("assets/cards/assistants/" + id + ".png");
    }

    /**
     * asset retrieve for character cards
     * @param id type to get image for
     * @return the Image object
     */
    private static Image getCharacterImageFromId (int id) {
        return new Image("assets/cards/characters/" + id + ".png");
    }

    /**
     * asset retrieve for ban cards
     * @return the Image object
     */
    private static Image getNoEntryImage () {
        return new Image("assets/ban.png");
    }

    /**
     * updates the status of the islands
     * @param board board to update
     * @param client client to get status from
     */
    private static void updateIslands (BoardRecord board, Client client) {
        // update islands
        for (IslandGroup group : client.getModel().getIslandGroupList()) {
            boolean first = true;
            for (Island island : group.getIslandList()) {
                IslandRecord islandRecord = board.islands().get(island.getId());

                final int yellow = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count();
                final int blue = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count();
                final int green = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count();
                final int red = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count();
                final int pink = (int) island.getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count();
                final List<Integer> colorCounts = Arrays.asList(yellow, blue, green, red, pink);

                IntStream.range(0, 5).forEach(i -> {
                    islandRecord.pawns().get(i).setVisible(colorCounts.get(i) > 0);
                    islandRecord.counts().get(i).setVisible(colorCounts.get(i) > 0);
                    islandRecord.counts().get(i).setText(String.valueOf(colorCounts.get(i)));

                });

                if(client.getModel().getFistIslandGroup().equals(group) && first) {
                    first = false;
                    islandRecord.motherNature().setVisible(true);
                }
                else {
                    islandRecord.motherNature().setVisible(false);
                }

                islandRecord.noEntry().setVisible(island.hasStopCard());

                if (group.getTower() != null) {
                    islandRecord.tower().setImage(getTowerImageFromColor(group.getTower()));
                    islandRecord.tower().setVisible(true);
                }
                else {
                    islandRecord.tower().setVisible(false);
                }

            }
        }
    }

    /**
     * checks if the islands are part of a common group
     * @param model model of the game
     * @param firstIsl first island id
     * @param secondIsl second island id
     * @return true if the islands need to be connected with a bridge, false otherwise
     */
    private static boolean areIslandsConnected (Game model, int firstIsl, int secondIsl) {
        for (IslandGroup g : model.getIslandGroupList()) {
            if (2 == g.getIslandList().stream().map(Island::getId).filter(integer -> integer == firstIsl || integer == secondIsl).count()) {
                return true;
            }
        }
        return false;
    }

    /**
     * create the bridge between islands if necessary
     * @param board the board to update
     * @param client the client to get the status from
     */
    private static void updateBridges (BoardRecord board, Client client) {
        IntStream.range(0, 12).forEach(i -> {
            int j = i == 11 ? 0 : i + 1;
            board.bridges().get(i).setVisible(areIslandsConnected(client.getModel(), i, j));
        });
    }

    /**
     * updates the clouds content
     * @param board the board to update
     * @param client the client to get the status from
     */
    private static void updateClouds (BoardRecord board, Client client) {
        int i = 0;
        for (Cloud cloud : client.getModel().getCloudList()) {
            int j = 0;
            CloudRecord cloudRecord = board.clouds().get(i);
            for (StudentColor student : cloud.getCloudContent()) {
                cloudRecord.pawns().get(j).setImage(getStudentImageFromColor(student));
                cloudRecord.pawns().get(j).setVisible(true);
                j++;
            }
            for (; j < 4; j++) {
                cloudRecord.pawns().get(j).setVisible(false);
            }
            i++;
        }

        // set not visible the cloud not active
        for (; i < 4; i++) {
            CloudRecord cloudRecord = board.clouds().get(i);
            cloudRecord.cloudImage().setVisible(false);
            IntStream.range(0, 4).forEach(z -> cloudRecord.pawns().get(z).setVisible(false));
        }
    }

    /**
     * updates the dining room of the player
     * @param wizard id of the player
     * @param school record of the board of the player
     * @param isMyBoard flag to indicate if the board belong to the player
     */
    private static void updateDining (Wizard wizard, SchoolBoardRecord school, boolean isMyBoard) {
        int[] diningColors = new int[]{
                wizard.getDiningStudents(StudentColor.YELLOW),
                wizard.getDiningStudents(StudentColor.BLUE),
                wizard.getDiningStudents(StudentColor.GREEN),
                wizard.getDiningStudents(StudentColor.RED),
                wizard.getDiningStudents(StudentColor.PINK)
        };

        int i = 0;
        for (FlowPane flowPane : school.dining()) {
            if (isMyBoard) {
                // padding left
                flowPane.setPadding(new Insets(0, 0, 0, 4));
                // horizontal gap between students
                flowPane.setHgap(4.5);
            }
            else {
                // padding bottom
                flowPane.setPadding(new Insets(0, 0, 4, 0));
                // vertical gap between students
                flowPane.setVgap(4.5);
            }

            // retrieving the observable list of the flow Pane
            ObservableList<Node> list = flowPane.getChildren();
            list.clear();

            //Adding all the nodes to the flow pane
            for (int j = 0; j < diningColors[i]; j++) {
                ImageView imageView = new ImageView(getStudentImageFromColor(StudentColor.fromNumber(i)));
                imageView.setFitWidth(24);
                imageView.setFitHeight(24);
                list.add(imageView);
            }
            i++;
        }
    }

    /**
     * updates the school content
     * @param board the board to update
     * @param client the client to get the status from
     * @param username the username of the player
     */
    public static void updateSchoolBoard (BoardRecord board, Client client, String username) {
        SchoolBoardRecord school;
        boolean isMyBoard = username.equals(client.getUsername());

        if (isMyBoard) {
            school = board.myBoard();
        }
        else {
            school = board.otherBoard();
        }

        Wizard wizard = client.getModel().getWizard(client.getUsernames().indexOf(username));

        // update entrance
        int i = 0;
        for (StudentColor student : wizard.getEntranceStudents()) {
            school.entrance().get(i).setImage(getStudentImageFromColor(student));
            school.entrance().get(i).setVisible(true);
            i++;
        }
        for (; i < 9; i++) {
            school.entrance().get(i).setVisible(false);
        }

        // update dining
        updateDining(wizard, school, isMyBoard);

        // update professors
        Game model = client.getModel();
        boolean[] professors = new boolean[]{
                model.getProfessor(StudentColor.YELLOW).getMaster() != null && model.getProfessor(StudentColor.YELLOW).getMaster().equals(wizard),
                model.getProfessor(StudentColor.BLUE).getMaster() != null && model.getProfessor(StudentColor.BLUE).getMaster().equals(wizard),
                model.getProfessor(StudentColor.GREEN).getMaster() != null && model.getProfessor(StudentColor.GREEN).getMaster().equals(wizard),
                model.getProfessor(StudentColor.RED).getMaster() != null && model.getProfessor(StudentColor.RED).getMaster().equals(wizard),
                model.getProfessor(StudentColor.PINK).getMaster() != null && model.getProfessor(StudentColor.PINK).getMaster().equals(wizard)
        };
        i = 0;
        for (ImageView professor : school.professors()) {
            professor.setVisible(professors[i]);
            i++;
        }

        // update towers
        Tower towerColor = wizard.getTowerColor();
        int towerNumber = wizard.getTowerNumber();
        if (client.getModel().getPlayerNumber().equals(PlayerNumber.FOUR)) {
            towerNumber = towerNumber / 2 + (wizard.getId() > 1 ? towerNumber % 2 : 0);
        }
        for (i = 0; i < 8; i++) {
            school.towers().get(i).setVisible(i < towerNumber);
            if (i < towerNumber) school.towers().get(i).setImage(getTowerImageFromColor(towerColor));
        }

        // draw arrows
        List<String> usersNotMyUser = new ArrayList<>(client.getUsernames());
        usersNotMyUser.remove(client.getUsername());

        if (!isMyBoard) {
            for (ImageView arrow : board.arrows()) {
                arrow.setVisible(board.arrows().indexOf(arrow) == usersNotMyUser.indexOf(username));
            }
        }

    }

    /**
     * updates the user area content of the other players
     * @param board the board to update
     * @param client the client to get the status from
     */
    private static void updateUsersArea (BoardRecord board, Client client) {
        int indexOfMyUser = client.getUsernames().indexOf(client.getUsername());

        int i = 0;
        for (String user : client.getUsernames()) {
            Wizard wizard = client.getModel().getWizard(client.getUsernames().indexOf(user));

            UserRecord userRecord;
            if (wizard.getId() == indexOfMyUser) {
                userRecord = board.users().get(0);
            }
            else {
                userRecord = board.users().get(wizard.getId() > indexOfMyUser ? wizard.getId() : wizard.getId() + 1);
            }

            // set username
            userRecord.username().setText(user);

            // set wizard cover image
            userRecord.wizard().setImage(getWizardImageFromId(wizard.getId() + 1));

            // set assistant
            if (wizard.getCardDeck().getCurrentCard() != null) {
                userRecord.assistant().setImage(getAssistantImageFromId(wizard.getCardDeck().getCurrentCard().getNumber()));
                userRecord.assistant().setVisible(true);
            }
            else {
                userRecord.assistant().setVisible(false);
            }

            // set hourglass if the user is playing
            userRecord.hourGlass().setVisible(client.getModel().getGameState().getCurrentPlayer().equals(wizard.getId()));

            // display coin image and set amount
            if (client.getModel().getGameMode().equals(GameMode.COMPLETE)) {
                userRecord.coinNumber().setText(String.valueOf(wizard.getMoney()));
                userRecord.coinImage().setVisible(true);
            }
            else {
                userRecord.coinNumber().setVisible(false);
                userRecord.coinImage().setVisible(false);
            }

            // set disconnected image
            if (wizard.getId() != indexOfMyUser)
                userRecord.disconnected().setVisible(client.getUsernamesDisconnected().contains(user));

            i++;
        }

        // hide the other users
        for (; i < 4; i++) {
            UserRecord userRecord = board.users().get(i);
            userRecord.assistant().setVisible(false);
            userRecord.username().setVisible(false);
            userRecord.wizard().setVisible(false);
            userRecord.coinNumber().setVisible(false);
            userRecord.disconnected().setVisible(false);
            userRecord.hourGlass().setVisible(false);
            userRecord.coinImage().setVisible(false);
        }
    }

    /**
     * updates the assistant card available content
     * @param board the board to update
     * @param client the client to get the status from
     */
    private static void updateDeck (BoardRecord board, Client client) {
        Wizard wizard = client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername()));
        int[] cards = wizard.getCardDeck().getDeckCards();

        for (int i = 1; i <= 10; i++) {
            boolean found = false;
            for (int x : cards) {
                if (x == i) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                board.myDeck().get(i - 1).setVisible(false);
            }
        }

    }

    /**
     * updates the characters content
     * @param board the board to update
     * @param client the client to get the status from
     */
    private static void updateCharacters (BoardRecord board, Client client) {
        if (client.getModel().getGameMode().equals(GameMode.COMPLETE)) {
            for (int i = 0; i < 3; i++) {
                CharacterRecord characterRecord = board.characters().get(i);
                Character currCharacter = client.getModel().getCharacters()[i];

                // set image
                characterRecord.card().setImage(getCharacterImageFromId(currCharacter.getId()));
                characterRecord.card().setVisible(true);

                // set coins
                characterRecord.money().setText(String.valueOf(currCharacter.getPrize()));

                // set correct items
                List<ImageView> items = characterRecord.items();
                switch (currCharacter.getId()) {
                    case 2, 3, 4, 6, 8, 10 -> {
                        for (ImageView item : items) {
                            item.setVisible(false);
                        }
                    }
                    case 1 -> {
                        int j = 0;
                        for (StudentColor student : ((CharacterOne) currCharacter).getStudentColorList()) {
                            items.get(j).setImage(getStudentImageFromColor(student));
                            items.get(j).setVisible(true);
                            j++;
                        }
                        for (; j < 6; j++) {
                            items.get(j).setVisible(false);
                        }
                    }
                    case 7 -> {
                        int j = 0;
                        for (StudentColor student : ((CharacterSeven) currCharacter).getStudentColorList()) {
                            items.get(j).setImage(getStudentImageFromColor(student));
                            items.get(j).setVisible(true);
                            j++;
                        }
                        for (; j < 6; j++) {
                            items.get(j).setVisible(false);
                        }
                    }
                    case 9, 12 -> {
                        int j = 0;
                        for (StudentColor student : StudentColor.values()) {
                            items.get(j).setImage(getStudentImageFromColor(student));
                            items.get(j).setVisible(true);
                            j++;
                        }
                        items.get(5).setVisible(false);
                    }
                    case 11 -> {
                        int j = 0;
                        for (StudentColor student : ((CharacterEleven) currCharacter).getStudentColorList()) {
                            items.get(j).setImage(getStudentImageFromColor(student));
                            items.get(j).setVisible(true);
                            j++;
                        }
                        for (; j < 6; j++) {
                            items.get(j).setVisible(false);
                        }
                    }
                    case 5 -> {
                        int j = 0;
                        for (; j < ((CharacterFive) currCharacter).getStopNumber(); j++) {
                            items.get(j).setImage(getNoEntryImage());
                            items.get(j).setVisible(true);
                        }
                        for (; j < 6; j++) {
                            items.get(j).setVisible(false);
                        }
                    }
                }
                if ((Objects.equals(client.getModel().getGameState().getGameStateName(), "MSS") || Objects.equals(client.getModel().getGameState().getGameStateName(), "MMNS") || Objects.equals(client.getModel().getGameState().getGameStateName(), "CCS")) &&
                        ((ActionState) client.getModel().getGameState()).getActivatedCharacter() == currCharacter) {
                    board.characters().get(i).card().setStyle("-fx-effect : dropshadow(gaussian, red, 4, 1, 0, 0);");
                } else {
                    board.characters().get(i).card().setStyle("");
                }
            }
        }
        // hide characters
        else {
            for (CharacterRecord characterRecord : board.characters()) {
                characterRecord.card().setVisible(false);
                characterRecord.moneyImage().setVisible(false);
                characterRecord.money().setVisible(false);
                for (ImageView item : characterRecord.items()) {
                    item.setVisible(false);
                }
            }
        }
    }

    /**
     * updates the info of the match
     * @param board the board to update
     * @param client the client to get the status from
     */
    public static void updateBoard (BoardRecord board, Client client, Integer selectedOtherUser) {
        updateIslands(board, client);

        updateBridges(board, client);

        updateClouds(board, client);

        // mySchoolBoard
        updateSchoolBoard(board, client, client.getUsername());

        // otherSchoolBoard
        List<String> usersNotMyUser = new ArrayList<>(client.getUsernames());
        usersNotMyUser.remove(client.getUsername());
        String currPlayerClient = client.getUsernames().get(client.getModel().getGameState().getCurrentPlayer());
        if (selectedOtherUser != null) {
            updateSchoolBoard(board, client, usersNotMyUser.get(selectedOtherUser - 1));

        }
        else if (usersNotMyUser.contains(currPlayerClient)) {
            updateSchoolBoard(board, client, currPlayerClient);
        }
        else {
            updateSchoolBoard(board, client, usersNotMyUser.get(0));
        }

        updateUsersArea(board, client);

        updateDeck(board, client);

        board.round().setText(String.valueOf(findRoundNumber(client.getModel())));

        switch (client.getModel().getGameState().getGameStateName()) {
            case "PS" -> board.phase().setText("Select card");
            case "MSS" -> board.phase().setText("Move student");
            case "MMNS" -> board.phase().setText("Move nature");
            case "CCS" -> board.phase().setText("Choose cloud");
        }

        updateCharacters(board, client);
    }
}

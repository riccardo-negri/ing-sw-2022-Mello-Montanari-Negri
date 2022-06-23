package it.polimi.ingsw.client.ui.gui.utils;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.gui.records.*;
import it.polimi.ingsw.model.entity.Cloud;
import it.polimi.ingsw.model.entity.Island;
import it.polimi.ingsw.model.entity.IslandGroup;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class BoardUtils {
    private BoardUtils() {
    }

    public static Object getElementFromNodesAndIdSubstring (List<Node> nodes, String idSubstring) {
        for (Node node : nodes) {
            String id = node.getId();
            if (id != null && id.contains(idSubstring)) {
                return node;
            }
        }
        return null;
    }

    private static Image getTowerImageFromColor (Tower tower) {
        return switch (tower) {
            case WHITE -> new Image("assets/pawns/towers/white.png");
            case BLACK -> new Image("assets/pawns/towers/black.png");
            case GRAY -> new Image("assets/pawns/towers/gray.png");
        };
    }

    private static Image getStudentImageFromColor (StudentColor color) {
        return switch (color) {
            case RED -> new Image("assets/pawns/students/3d/red.png");
            case BLUE -> new Image("assets/pawns/students/3d/blue.png");
            case PINK -> new Image("assets/pawns/students/3d/fuchsia.png");
            case GREEN -> new Image("assets/pawns/students/3d/green.png");
            case YELLOW -> new Image("assets/pawns/students/3d/yellow.png");
        };
    }

    private static Image getWizardImageFromId (int id) {
        return new Image("assets/cards/wizards/" + id + ".png");
    }

    private static Image getAssistantImageFromId (int id) {
        return new Image("assets/cards/assistants/" + id + ".png");
    }

    public static void updateIslands (BoardRecord board, Client client) {
        // update islands
        for (IslandGroup group : client.getModel().getIslandGroupList()) {
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

                islandRecord.motherNature().setVisible(client.getModel().getFistIslandGroup().equals(group));

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

    public static void updateClouds (BoardRecord board, Client client) {
        int i = 0;
        for (Cloud cloud : client.getModel().getCloudList()) {
            int j = 0;
            CloudRecord cloudRecord = board.clouds().get(i);
            for (StudentColor student : cloud.getCloudContent()) {
                cloudRecord.pawns().get(j).setImage(getStudentImageFromColor(student));
                cloudRecord.pawns().get(j).setVisible(true);
                j++;
            }
            if (j == 3) cloudRecord.pawns().get(3).setVisible(false);
            i++;
        }

        // set not visible the cloud not active
        for (; i < 4; i++) {
            CloudRecord cloudRecord = board.clouds().get(i);
            cloudRecord.cloudImage().setVisible(false);
            IntStream.range(0, 4).forEach(z -> cloudRecord.pawns().get(z).setVisible(false));
        }
    }

    public static void updateSchoolBoard(BoardRecord board, Client client, String username) {
        SchoolBoardRecord school;
        if (username.equals(client.getUsername())) {
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

        // update professors

        // update towers
    }

    public static void updateUsersArea (BoardRecord board, Client client) {
        int indexOfMyUser = client.getUsernames().indexOf(client.getUsername());
        int i = 0;
        for (String user: client.getUsernames()) {
            Wizard wizard = client.getModel().getWizard(client.getUsernames().indexOf(user));

            UserRecord userRecord;
            if(wizard.getId() == indexOfMyUser) userRecord = board.users().get(0);
            else userRecord = board.users().get(wizard.getId() > indexOfMyUser ? wizard.getId() : wizard.getId()+1);

            // set username
            userRecord.username().setText(user);

            // set wizard cover image
            userRecord.wizard().setImage(getWizardImageFromId(wizard.getId()+1));

            // set assistant
            if (wizard.getCardDeck().getCurrentCard() != null) {
                userRecord.assistant().setImage(getAssistantImageFromId(wizard.getCardDeck().getCurrentCard().getNumber()));
                userRecord.assistant().setVisible(true);
            }
            else {
                userRecord.assistant().setVisible(false);
            }
            i++;
        }

        // hide the other assistants
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

    public static void updateBoard (BoardRecord board, Client client) {

        updateIslands(board, client);

        updateClouds(board, client);

        // mySchoolBoard
        updateSchoolBoard(board, client, client.getUsername());
        // otherSchoolBoard
        String otherUsername = client.getUsernames().get(client.getUsernames().indexOf(client.getUsername()) == 0 ? 1 : 0);
        updateSchoolBoard(board, client, otherUsername);

        updateUsersArea(board, client);
        // display only the correct amount of wizards and assistants
        for (int i = 4; i > client.getUsernames().size(); i--) {
            UserRecord user = board.users().get(i - 1);
            user.wizard().setVisible(false);
            user.assistant().setVisible(false);
            user.hourGlass().setVisible(false);
            user.disconnected().setVisible(false);
            if (client.isAdvancedGame()) user.coinNumber().setVisible(false);
        }
    }
}

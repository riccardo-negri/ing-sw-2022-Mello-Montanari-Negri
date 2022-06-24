package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BoardPageGUI extends AbstractBoardPage {

    private int studentPicked;

    protected BoardPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Game", "/fxml/BoardPage.fxml", new BoardPageController());
    }

    public void handleStudentPick (int number) {
        if (!Objects.equals(model.getGameState().getGameStateName(), "MSS")) logger.log(Level.INFO, "Player " + client.getUsername() + ": Wrong game phase to pick student");
        else {
            if (studentPicked == number) studentPicked = -1;
            else studentPicked = number;
        }
    }

    public void handleAssistantCard(int card) {
        switch (model.getGameState().getGameStateName()) {
            case "PS":
                try {
                    doCardChoice(card);
                } catch (Exception e) {
                    client.getLogger().log(Level.INFO, e.getMessage());
                } break;
        }

    }

    public void handleDiningTable(StudentColor color) {
        switch (model.getGameState().getGameStateName()) {
            case "MSS":
                if (studentPicked == -1 || model.getWizard(client.getUsernames().indexOf(client.getUsername())).getEntranceStudents().get(studentPicked) != color)
                    logger.log(Level.INFO, "Move of student " + model.getWizard(client.getPlayerNumber()).getEntranceStudents().get(studentPicked).toString() + " to dining table " + color.toString() + " not correct");
                else {
                    try {
                        doStudentMovement(color, "dining-room");
                        studentPicked = -1;
                    } catch (Exception e) {
                        logger.log(Level.INFO, e.getMessage());
                    }
                } break;
        }
    }

    public void handleIsland(int islandId) {
        switch (model.getGameState().getGameStateName()) {
            case "MSS":
                if (studentPicked == -1) logger.log(Level.INFO, "No student selected in entrance");
                else { try {
                        doStudentMovement(model.getWizard(client.getUsernames().indexOf(client.getUsername())).getEntranceStudents().get(studentPicked), "island-" + islandId);
                        studentPicked = -1;
                    } catch (Exception e) {
                        logger.log(Level.INFO, e.getMessage());
                    }
                } break;
            case "MMNS":
                try { doMotherNatureMovement(model.getIslandGroupList().indexOf(
                        model.getIslandGroupList().stream().filter(islandGroup ->
                                islandGroup.getIslandList().contains(model.getIsland(islandId))).toList().get(0)));
                } catch (Exception e) {
                    logger.log(Level.INFO, e.getMessage());
                }
        }
    }

    public void handleCloud(int cloudId) {
        switch (model.getGameState().getGameStateName()) {
            case "CCS":
                try {
                    doCloudChoice(cloudId);
                } catch (Exception e) {
                    logger.log(Level.INFO, e.getMessage());
                } break;
        }
    }
}

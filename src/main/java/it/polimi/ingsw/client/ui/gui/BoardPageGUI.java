package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.Objects;
import java.util.logging.Level;

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
        try {
            doCardChoice(card);
        } catch (Exception e) {
            client.getLogger().log(Level.INFO, e.getMessage());
        }
    }

    public void handleDiningTable(StudentColor color) {
        if (studentPicked == -1 || model.getWizard(client.getUsernames().indexOf(client.getUsername())).getEntranceStudents().get(studentPicked) != color)
            System.out.println("Move of student " + model.getWizard(client.getPlayerNumber()).getEntranceStudents().get(studentPicked).toString() + " to dining table " + color.toString() + " not correct");
        else {
            try {
                doStudentMovement(color, "dining-room");
                studentPicked = -1;
            } catch (Exception e) {
                client.getLogger().log(Level.INFO, e.getMessage());
            }
        }
    }

    public void handleIsland(int islandId) {
        if (studentPicked == -1)
            logger.log(Level.INFO, "No student selected in entrance");
        else {
            try {
                doStudentMovement(model.getWizard(client.getUsernames().indexOf(client.getUsername())).getEntranceStudents().get(studentPicked), "island-" + islandId);
                studentPicked = -1;
            } catch (Exception e) {
                client.getLogger().log(Level.INFO, e.getMessage());
            }
        }
    }
}

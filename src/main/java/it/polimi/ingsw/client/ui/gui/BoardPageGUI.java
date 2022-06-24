package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.client.ui.gui.records.BoardRecord;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.moves.*;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BoardPageGUI extends AbstractBoardPage {

    private int studentPicked = -1;
    private Integer[][] characterStudentPicked;

    private boolean activatedCharacter[] = new boolean[3];

    protected BoardPageGUI(Client client) {
        super(client);
        for(int i=0; i<3; i++) {
            for (int j=0; j<3; j++)
                characterStudentPicked[i][j] = -1;
            activatedCharacter[i] = false;
        }
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Game", "/fxml/BoardPage.fxml", new BoardPageController());
    }

    public int getStudentPicked() { return studentPicked; }

    public void setStudentPicked(int studentPicked) { this.studentPicked = studentPicked; }

    public int getCharacterStudentPicked(int characterNumber, int index) {
        return characterStudentPicked[characterNumber][index];
    }

    public void setCharacterStudentPicked(int characterNumber, int index, int characterStudentValue) {
        characterStudentPicked[characterNumber][index] = characterStudentValue;
    }

    public boolean getActivatedCharacter(int characterNumber) {
        return activatedCharacter[characterNumber];
    }

    public void setActivatedCharacter (int characterNumber, boolean status) {
        activatedCharacter[characterNumber] = status;
    }
}

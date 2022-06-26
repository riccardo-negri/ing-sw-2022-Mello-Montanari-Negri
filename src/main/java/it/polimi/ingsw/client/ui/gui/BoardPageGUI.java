package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

public class BoardPageGUI extends AbstractBoardPage {
    private int studentPicked = -1;
    private int islandPicked = -1;

    private final List<Integer> pickedCardStudents = new ArrayList<>();
    private final List<StudentColor> pickedDiningStudents = new ArrayList<>();
    private final List<Integer> pickedEntranceStudents = new ArrayList<>();
    private final List<Integer> pickedIslands = new ArrayList<>();
    private final List<StudentColor> pickedTables = new ArrayList<>();
    private final boolean[] activatedCharacter = new boolean[3];

    protected BoardPageGUI(Client client) {
        super(client);
        for(int i=0; i<3; i++) {
            activatedCharacter[i] = false;
        }
    }

    @Override
    public void draw() {
        showGUIPage("Game", "/fxml/BoardPage.fxml", new BoardPageController());
    }

    public int getStudentPicked() { return studentPicked; }

    public void setStudentPicked(int studentPicked) { this.studentPicked = studentPicked; }

    public void activateCharacter(int characterNumber) {
        setActivatedCharacter(characterNumber, true);
        pickedTables.clear();
        pickedIslands.clear();
        pickedEntranceStudents.clear();
        pickedDiningStudents.clear();
        pickedCardStudents.clear();
    }

    public boolean isCharacterActivated(int characterNumber) {
        return activatedCharacter[characterNumber];
    }

    public void setActivatedCharacter (int characterNumber, boolean status) {
        activatedCharacter[characterNumber] = status;
    }

    public boolean isAnyCharacterActivated() {
        return activatedCharacter[0] || activatedCharacter[1] || activatedCharacter[2];
    }

    public Integer getActivatedCharacterId() {
        if(activatedCharacter[0])
            return model.getCharacters()[0].getId();
        if(activatedCharacter[1])
            return model.getCharacters()[1].getId();
        if(activatedCharacter[2])
            return model.getCharacters()[2].getId();
        return null;
    }

    public List<StudentColor> getPickedDiningStudents() {
        return pickedDiningStudents;
    }

    public List<Integer> getPickedEntranceStudents() {
        return pickedEntranceStudents;
    }

    public List<Integer> getPickedCardStudents() {
        return pickedCardStudents;
    }

    public List<StudentColor> getPickedTables() {
        return pickedTables;
    }

    public List<Integer> getPickedIslands () { return pickedIslands; }

}

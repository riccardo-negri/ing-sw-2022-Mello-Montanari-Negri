package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.model.entity.Island;
import it.polimi.ingsw.model.enums.StudentColor;

import java.util.ArrayList;
import java.util.List;

public class BoardPageGUI extends AbstractBoardPage {
    private int studentPicked = -1;

    private int cardStudentsToPick = 0;
    private int diningStudentsToPick = 0;
    private int entranceStudentsToPick = 0;
    private int islandsToPick = 0;
    private int tablesToPick = 0;

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

    public void activateCharacter(int characterNumber, int cardStudentsToPick, int diningStudentsToPick, int entranceStudentsToPick, int islandsToPick, int tablesToPick) {
        setActivatedCharacter(characterNumber, true);
        this.cardStudentsToPick = cardStudentsToPick;
        this.diningStudentsToPick = diningStudentsToPick;
        this.entranceStudentsToPick = entranceStudentsToPick;
        this.islandsToPick = islandsToPick;
        this.tablesToPick = tablesToPick;
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

    public boolean isEverythingNeededSelected() {
        return pickedCardStudents.size() == cardStudentsToPick &&
        pickedDiningStudents.size() == diningStudentsToPick &&
        pickedEntranceStudents.size() == entranceStudentsToPick &&
        pickedIslands.size() == islandsToPick &&
        pickedTables.size() == tablesToPick;
    }

    public List<Integer> getPickedIslands() {
        return pickedIslands;
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

    public int getIslandsToPick() {
        return islandsToPick;
    }

    public int getTablesToPick() {
        return tablesToPick;
    }

    public int getDiningStudentsToPick() {
        return diningStudentsToPick;
    }

    public int getCardStudentsToPick() {
        return cardStudentsToPick;
    }

    public int getEntranceStudentsToPick() {
        return entranceStudentsToPick;
    }
}

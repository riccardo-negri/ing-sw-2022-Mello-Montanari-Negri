package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.ui.gui.controllers.BoardPageController;
import it.polimi.ingsw.client.ui.gui.records.BoardRecord;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.moves.*;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BoardPageGUI extends AbstractBoardPage {

    private int studentPicked = -1;

    protected BoardPageGUI(Client client) {
        super(client);
    }

    @Override
    public void draw(Client client) {
        showGUIPage("Game", "/fxml/BoardPage.fxml", new BoardPageController());
    }

    public int getStudentPicked() { return studentPicked; }

    public void setStudentPicked(int studentPicked) { this.studentPicked = studentPicked; }
}

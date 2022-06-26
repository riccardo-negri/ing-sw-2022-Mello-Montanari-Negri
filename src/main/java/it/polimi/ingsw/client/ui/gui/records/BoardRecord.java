package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

public record BoardRecord(
        GridPane mainGrid,
        List<IslandRecord> islands,
        List<ImageView> bridges,
        List<CloudRecord> clouds,
        SchoolBoardRecord myBoard,
        SchoolBoardRecord otherBoard,
        List<UserRecord> users,
        List<CharacterRecord> characters,
        List<ImageView> myDeck,
        List<ImageView> arrows,
        Label round,
        Label phase
) {
}

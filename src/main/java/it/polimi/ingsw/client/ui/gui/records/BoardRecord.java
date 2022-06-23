package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public record BoardRecord(
        List<IslandRecord> islands,
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

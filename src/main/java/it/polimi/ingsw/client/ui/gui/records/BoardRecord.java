package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * references to all the elements of the board page
 * @param mainGrid
 * @param islands
 * @param bridges
 * @param clouds
 * @param myBoard
 * @param otherBoard
 * @param users
 * @param characters
 * @param myDeck
 * @param arrows
 * @param round
 * @param phase
 */
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

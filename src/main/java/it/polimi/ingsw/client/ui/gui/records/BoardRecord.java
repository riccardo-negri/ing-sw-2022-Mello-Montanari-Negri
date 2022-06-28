package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * references to all the elements of the board page
 * @param mainGrid main grid
 * @param islands islands
 * @param bridges bridges
 * @param clouds clouds
 * @param myBoard client board
 * @param otherBoard other users board
 * @param users users
 * @param characters characters
 * @param myDeck user's deck
 * @param arrows arrows
 * @param round round
 * @param phase phase
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

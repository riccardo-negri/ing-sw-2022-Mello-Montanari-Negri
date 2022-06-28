package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * references to all the elements of a character
 * @param card
 * @param moneyImage
 * @param money
 * @param items
 */
public record CharacterRecord(
        ImageView card,
        ImageView moneyImage,
        Label money,
        List<ImageView> items
) {
}

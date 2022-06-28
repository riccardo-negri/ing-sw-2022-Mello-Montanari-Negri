package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.image.ImageView;

import java.util.List;

/**
 * references to all the elements of a cloud
 * @param cloudImage cloud image
 * @param pawns pawns
 */
public record CloudRecord(
        ImageView cloudImage,
        List<ImageView> pawns
) {
}

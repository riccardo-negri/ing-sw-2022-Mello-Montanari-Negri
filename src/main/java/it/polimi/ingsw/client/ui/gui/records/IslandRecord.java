package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * references to all the elements of an island
 * @param tower tower
 * @param motherNature mother nature
 * @param noEntry no entry
 * @param land land
 * @param pawns pawns
 * @param counts count
 */
public record IslandRecord(
        ImageView tower,
        ImageView motherNature,
        ImageView noEntry,
        ImageView land,
        List<ImageView> pawns,
        List<Label> counts
) {
}

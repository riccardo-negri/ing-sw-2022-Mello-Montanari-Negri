package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * references to all the elements of an island
 * @param tower
 * @param motherNature
 * @param noEntry
 * @param land
 * @param pawns
 * @param counts
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

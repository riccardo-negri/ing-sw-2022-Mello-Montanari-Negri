package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public record IslandRecord(
        ImageView tower,
        ImageView motherNature,
        ImageView noEntry,
        List<ImageView> pawns,
        List<Label> counts
) {
}

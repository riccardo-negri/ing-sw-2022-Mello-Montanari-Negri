package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.image.ImageView;

import java.util.List;

public record CloudRecord(
        ImageView cloudImage,
        List<ImageView> pawns
) {
}

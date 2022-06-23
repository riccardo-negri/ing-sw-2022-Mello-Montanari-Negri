package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public record CharacterRecord(
        ImageView card,
        ImageView moneyImage,
        Label money,
        List<ImageView> items
) {
}

package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public record UserRecord(
        Label username,
        ImageView wizard,
        ImageView assistant,
        ImageView disconnected,
        ImageView hourGlass,
        ImageView coinImage,
        Label coinNumber
) {
}

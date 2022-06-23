package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.util.List;

public record SchoolBoardRecord(
        List<ImageView> entrance,
        List<FlowPane> dining,
        List<ImageView> professors,
        List<ImageView> towers
) {
}

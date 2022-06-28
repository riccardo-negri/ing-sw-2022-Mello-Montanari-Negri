package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.util.List;

/**
 * references to all the elements of a school
 * @param entrance entrance
 * @param dining dining
 * @param professors professors
 * @param towers towers
 */
public record SchoolBoardRecord(
        List<ImageView> entrance,
        List<FlowPane> dining,
        List<ImageView> professors,
        List<ImageView> towers
) {
}

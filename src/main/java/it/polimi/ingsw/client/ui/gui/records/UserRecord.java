package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * references to all the elements of a user
 * @param username
 * @param wizard
 * @param assistant
 * @param disconnected
 * @param hourGlass
 * @param coinImage
 * @param coinNumber
 */
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

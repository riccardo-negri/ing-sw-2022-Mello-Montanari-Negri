package it.polimi.ingsw.client.ui.gui.records;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * references to all the elements of a user
 * @param username username
 * @param wizard wizard
 * @param assistant assistant
 * @param disconnected disconnected
 * @param hourGlass hour glass
 * @param coinImage coin image
 * @param coinNumber coin number
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

package it.polimi.ingsw.networking;

/**
 * a message sent to all the connected clients to notify a user disconnected from the game
 * @param username the name of the disconnected user
 */
public record UserDisconnected(String username) implements Message {
}

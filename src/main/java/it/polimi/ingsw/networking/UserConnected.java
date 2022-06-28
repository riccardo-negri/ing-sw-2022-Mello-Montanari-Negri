package it.polimi.ingsw.networking;

/**
 * a message sent to all the connected clients to notify a new user joining the lobby or the game
 * @param username the name of the connected user
 */
public record UserConnected(String username) implements Message {
}

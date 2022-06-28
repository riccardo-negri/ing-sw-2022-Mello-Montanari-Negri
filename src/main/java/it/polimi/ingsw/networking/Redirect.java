package it.polimi.ingsw.networking;

/**
 * a message sent to the client to tell it the port of the game server that is assigned to
 * @param port the port of the game server
 */
public record Redirect(int port) implements Message {
}

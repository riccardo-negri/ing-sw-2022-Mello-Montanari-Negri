package it.polimi.ingsw.networking;

/**
 * a message sent from the client to the server to ask to be moved into a specific lobby
 * @param code the code of the lobby that the client wants to join
 */
public record LobbyChoice(String code) implements Message {
}

package it.polimi.ingsw.networking;

/**
 * a message sent preindirli on the connection that does nothing but resetting the socket timeout
 * if the connection doesn't receive any message for a while generates a Disconnected message
 */
public class Ping implements Message {
}

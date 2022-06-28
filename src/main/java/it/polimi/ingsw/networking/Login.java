package it.polimi.ingsw.networking;

/**
 * the first message expected from the server on a new connection, communicates the username of the connecting client
 * @param username the username of the connecting client
 */
public record Login(String username) implements Message {

    /**
     * get the value of username after removing newline characters
     * @return the username contained in the record in one line
     */
    @Override
    public String username() {
        // newline characters mess up when saving in file, removing them before using the username
        return username.replace("\n", "");
    }
}

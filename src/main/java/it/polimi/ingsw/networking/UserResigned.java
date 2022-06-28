package it.polimi.ingsw.networking;

/**
 * can be sent from the client to the server to communicate the intention to resign the game
 * in response the server sends it to all the connected clients to notify that the user resigned the game
 * this ends the game for all and causes the termination of the game server
 */
public class UserResigned implements Message {
    private String username;

    /**
     * a message that notifies of the decision to resign of a user
     * @param username the username of the user that resigned
     */
    public UserResigned(String username) {
        this.username = username;
    }

    /**
     * get username value
     * @return the username of the user that resigned
     */
    public String getUsername() {
        return username;
    }

    /**
     * set the value of username
     * @param username the username of the user that resigned
     */
    public void setUsername(String username) {
        this.username = username;
    }
}

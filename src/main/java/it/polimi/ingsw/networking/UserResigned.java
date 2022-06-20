package it.polimi.ingsw.networking;

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

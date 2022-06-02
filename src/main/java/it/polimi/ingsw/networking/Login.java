package it.polimi.ingsw.networking;

public class Login implements Message {
    final String username;

    public Login(String username) {
        this.username = username;
    }

    public String getUsername() {
        // newline characters mess up when saving in file, removing them before using the username
        return username.replace("\n", "");
    }
}

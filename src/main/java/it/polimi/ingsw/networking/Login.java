package it.polimi.ingsw.networking;

public class Login extends Message {
    final String username;

    public Login(String username) {
        this.username = username;
    }

    public String getUsername() {
        // newline characters mess up when saving in file, removing them before using the username
        return username.replace("\n", "");
    }
}

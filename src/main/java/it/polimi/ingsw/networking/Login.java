package it.polimi.ingsw.networking;

public record Login(String username) implements Message {

    @Override
    public String username() {
        // newline characters mess up when saving in file, removing them before using the username
        return username.replace("\n", "");
    }
}

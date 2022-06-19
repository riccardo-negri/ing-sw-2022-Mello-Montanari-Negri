package it.polimi.ingsw.networking;

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

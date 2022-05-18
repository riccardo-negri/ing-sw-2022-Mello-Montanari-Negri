package it.polimi.ingsw.networking;

public class Redirect extends Message {
    private final int port;

    public  Redirect(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}

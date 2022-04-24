package it.polimi.ingsw.utils;

public class Redirect extends Message{
    private final int port;

    public  Redirect(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}

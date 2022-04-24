package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 60000);
        OutputStream os = socket.getOutputStream();
        InputStream reader = socket.getInputStream();
    }
}

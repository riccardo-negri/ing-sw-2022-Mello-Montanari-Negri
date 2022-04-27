package it.polimi.ingsw.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class ConnectionBase {
    protected final Socket socket;
    protected final String targetAddress;

    protected Consumer<Connection> onNewMessage;

    protected final Thread thread;

    protected final ObjectInputStream reader;
    protected final ObjectOutputStream writer;

    public ConnectionBase(Socket socket, Consumer<Connection> onNewMessage) {
        try {
            this.socket = socket;
            this.targetAddress = String.valueOf(socket.getInetAddress());
            this.onNewMessage = onNewMessage;
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            thread = new Thread(this::listenMessages);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void listenMessages();
}

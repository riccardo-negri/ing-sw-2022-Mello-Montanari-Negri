package it.polimi.ingsw.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ConnectionBase {
    protected final Socket socket;
    protected final String targetAddress;

    protected Predicate<Connection> acceptMessage;

    protected final Thread thread;

    protected final ObjectInputStream reader;
    protected final ObjectOutputStream writer;

    public ConnectionBase(Socket socket, Predicate<Connection> acceptMessage) {
        try {
            this.socket = socket;
            this.targetAddress = String.valueOf(socket.getInetAddress());
            this.acceptMessage = acceptMessage;
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

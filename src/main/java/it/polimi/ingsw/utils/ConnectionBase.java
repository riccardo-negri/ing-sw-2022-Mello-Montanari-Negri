package it.polimi.ingsw.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Predicate;

public abstract class ConnectionBase {
    protected final SafeSocket socket;

    protected final ConnectionPredicate acceptMessage;

    protected final Thread thread;

    protected final ObjectInputStream reader;
    protected final ObjectOutputStream writer;

    public ConnectionBase(SafeSocket socket, Predicate<Connection> acceptMessage) {
        try {
            this.socket = socket;
            this.acceptMessage = new ConnectionPredicate(acceptMessage);
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

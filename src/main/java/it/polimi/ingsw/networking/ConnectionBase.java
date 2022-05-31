package it.polimi.ingsw.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.logging.Logger;

public abstract class ConnectionBase {

    // these periods are expressed in seconds
    private static final int pingPeriod = 20;
    private static final int disconnectPeriod = 30;
    protected final SafeSocket socket;

    protected final ConnectionPredicate acceptMessage;

    protected final Thread thread;
    Timer pingTimer = new Timer();

    protected final ObjectInputStream reader;
    protected final ObjectOutputStream writer;

    protected final Logger logger;

    public ConnectionBase(SafeSocket socket, Predicate<Connection> acceptMessage, Logger logger) {
        try {
            socket.setSoTimeout(disconnectPeriod*1000);
            this.socket = socket;
            this.acceptMessage = new ConnectionPredicate(acceptMessage);
            this.logger = logger;
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            thread = new Thread(this::listenMessages);
            TimerTask pingTask = new TimerTask() {
                public void run() {send(new Ping());}
            };
            thread.start();
            pingTimer.scheduleAtFixedRate(pingTask, pingPeriod *1000, pingPeriod *1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void listenMessages();

    public abstract void send(Message message);
}

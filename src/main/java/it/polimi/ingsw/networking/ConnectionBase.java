package it.polimi.ingsw.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ConnectionBase {

    // these periods are expressed in seconds
    private static final int PING_PERIOD = 20;
    private static final int DISCONNECT_PERIOD = 30;
    protected final SafeSocket socket;

    protected final ConnectionPredicate acceptMessage;

    protected final Thread thread;
    final Timer pingTimer = new Timer();

    protected final ObjectInputStream reader;
    protected final ObjectOutputStream writer;

    protected final Logger logger;

    protected ConnectionBase(SafeSocket socket, Predicate<Connection> acceptMessage, Logger logger) {
        this.socket = socket;
        this.acceptMessage = new ConnectionPredicate(acceptMessage);
        this.logger = logger;
        ObjectOutputStream w = null;
        ObjectInputStream r = null;
        try {
            this.socket.setSoTimeout(DISCONNECT_PERIOD *1000);
            w = new ObjectOutputStream(socket.getOutputStream());
            r = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            String toLog = "IOException when  starting connection: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
        }
        writer = w;
        reader = r;
        thread = new Thread(this::listenMessages);
        TimerTask pingTask = new TimerTask() {
            public void run() {send(new Ping());}
        };
        thread.start();
        pingTimer.scheduleAtFixedRate(pingTask, (long) PING_PERIOD * 1000, (long) PING_PERIOD * 1000);
    }

    protected abstract void listenMessages();

    public abstract void send(Message message);

    public SafeSocket getSocket() {
        return socket;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public ObjectInputStream getReader() {
        return reader;
    }
}

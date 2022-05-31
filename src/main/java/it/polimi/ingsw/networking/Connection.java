package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.moves.Move;
import it.polimi.ingsw.utils.Counter;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection extends ConnectionBase {
    private final Counter movesCount = new Counter();

    private final MovesQueue movesQueue = new MovesQueue();
    private final Queue<Message> messagesToProcess = new LinkedList<>();

    public Connection(SafeSocket socket, Predicate<Connection> acceptMessage, Logger logger) {
        super(socket, acceptMessage, logger);

    }

    public Connection(SafeSocket socket, Logger logger) {
        super(socket, Connection::doNothing, logger);

    }

    public Connection(String address, int port, Predicate<Connection> acceptMessage, Logger logger) {
        super(Connection.createSocket(address, port), acceptMessage, logger);

    }

    public Connection(String address, int port, Logger logger) {
        super(Connection.createSocket(address, port), Connection::doNothing, logger);
    }

    static private boolean doNothing(Connection source) {return false;}

    static private SafeSocket createSocket(String address, int port) {
        try {
            return new SafeSocket(new Socket(address, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bindFunction(Predicate<Connection> acceptMessage) {
        this.acceptMessage.set(acceptMessage);
    }

    public synchronized void bindFunctionAndTestPrevious(Predicate<Connection> acceptMessage) {
        bindFunction(acceptMessage);

        // process the messages received before the call of bindFunction
        int steps = messagesToProcess.size();
        for (int i = 0; i < steps; i++) {  // roll the queue on itself until is back to start
            if(acceptMessage.test(this)) {  // test last element
                messagesToProcess.poll();
            } else {
                messagesToProcess.add(messagesToProcess.poll());
            }
        }
    }

    protected void listenMessages() {
        String toLog = "Listening for new messages from: " + socket.getInetAddress();
        logger.log(Level.INFO, toLog);
        while (isRunning()) {
            try {
                Message msg = (Message) reader.readObject();
                if (msg instanceof Move) {
                    updateQueue((Move) msg);
                }
                else if(!(msg instanceof Ping)) {
                    processMessage(msg);
                }
            } catch(EOFException | SocketTimeoutException e) {
                // EOF means that the connection was closed from the other end
                // SocketTimeout means is not receiving the expected ping, so it means the connection is lost
                stop();
                processMessage(new Disconnected());
                return;
            } catch(ClassNotFoundException | IOException | ClassCastException ignored) {
                // IOException contains also SocketException which means I called close() on this socket
            }
        }
    }

    /*
    The queue is used to make sure the moves are processed in the correct order, follows this procedure:
    Add the new move to the queue in the correct position
    Process all the moves in the queue following the order they were sent, remove them when done
    If on move is missing stops processing and will continue from there on the next call
     */
    void updateQueue(Move move) {
        movesQueue.add(move);
        for (Move m: movesQueue.pollAvailable()) {
            processMessage(m);
        }
    }

    private synchronized void processMessage(Message message) {
        String toLog = "Received new object from " + socket.getInetAddress() + ": " + message;
        logger.log(Level.INFO, toLog);
        messagesToProcess.add(message);
        if(acceptMessage.test(this)) {
            messagesToProcess.poll();  // remove the only message accessible by acceptMessage
        }
        notifyAll();
    }

    static boolean matchFileter(Message message, List<Class> filter) {
        for (Class c: filter) {
            if(c.isInstance(message))
                return true;
        }
        return false;
    }

    public Message waitMessage() {
        return waitMessage(Message.class);
    }

    public Message waitMessage(Class filter) {
        List<Class> filterList = new ArrayList<>();
        filterList.add(filter);
        return waitMessage(filterList);
    }

    public synchronized Message waitMessage(List<Class> filter) {
        for (Message m: messagesToProcess) {  // if message was received before the call of waitMessage
            if(matchFileter(m, filter)) {
                messagesToProcess.remove(m);
                return m;
            }
        }
        while (!matchFileter(getLastMessage(), filter)) {  // if no compatible found wait for one
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return messagesToProcess.poll();
    }

    public synchronized Message getLastMessage () {
        return messagesToProcess.peek();
    }

    public void send(Message message) {
        try {
            Move move = (Move) message;
            int next = movesCount.increment();
            move.setNumber(next-1);
        } catch (ClassCastException ignored) {}
        try {
            synchronized (writer) {
                writer.writeObject(message);
            }
        } catch (IOException ignored) {}
    }

    boolean isRunning() {
        return !socket.isClosed();
    }

    void stop() {
        try {
            socket.close();
        } catch (IOException ignored) {}
        pingTimer.cancel();
    }

    public void close() {
        try {
            stop();
            if (!Thread.currentThread().equals(thread))
                thread.join();
        } catch (InterruptedException ignored) {}
    }
}

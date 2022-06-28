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

/**
 * manages the socket interaction to expose simpler functions to receive and send messages
 * the connection is always listening for messages and puts them in a queue
 * messages from the connection can be waited with blocking functions or read as soon as they arrive from callback functions
 */
public class Connection extends ConnectionBase {
    private final Counter movesCount = new Counter();

    private final MovesQueue movesQueue = new MovesQueue();
    private final Queue<Message> messagesToProcess = new LinkedList<>();

    private static final Predicate<Connection> DO_NOTHING = c -> false;

    /**
     * uses the ConnectionBase constructor
     * @param socket the socket this connection will use to send and receive messages
     * @param acceptMessage the callback function to execute when a message is received
     * @param logger the connection debug info will be sent to logger
     */
    public Connection(SafeSocket socket, Predicate<Connection> acceptMessage, Logger logger) {
        super(socket, acceptMessage, logger);

    }

    /**
     * create a connection with no callback on new message
     * @param socket the socket this connection will use to send and receive messages
     * @param logger the connection debug info will be sent to logger
     */
    public Connection(SafeSocket socket, Logger logger) {
        super(socket, DO_NOTHING, logger);

    }

    /**
     * creates connection with socket from address and port
     * @param address the target ip to connect to
     * @param port the remote port to connect to
     * @param acceptMessage the callback function to execute when a message is received
     * @param logger the connection debug info will be sent to logger
     */
    public Connection(String address, int port, Predicate<Connection> acceptMessage, Logger logger) {
        super(Connection.createSocket(address, port, logger), acceptMessage, logger);

    }

    /**
     * create a connection with no callback on new message and with socket from address and port
     * @param address the target ip to connect to
     * @param port the remote port to connect to
     * @param logger the connection debug info will be sent to logger
     */
    public Connection(String address, int port, Logger logger) {
        super(Connection.createSocket(address, port, logger), DO_NOTHING, logger);
    }

    /**
     * creates socket from address and port
     * @param address the target ip to connect to
     * @param port the remote port to connect to
     * @param logger eventual error message will be sent to logger
     * @return the socket just created already bounded to the remote one
     */
    private static SafeSocket createSocket(String address, int port, Logger logger) {
        SafeSocket result = null;
        try {
            result = new SafeSocket(new Socket(address, port));
        } catch (IOException e) {
            String toLog = "Failed to create connection socket: " + e.getMessage();
            logger.log(Level.SEVERE, toLog);
        }
        return result;
    }

    /**
     * set a new callback function to run on future received messages
     * @param acceptMessage the new function Connection->Boolean, the function can read connection information and take
     *                      the first message from it, if the message is processed and therefore should be consumed
     *                      returns true, else returns false
     */
    public void bindFunction(Predicate<Connection> acceptMessage) {
        this.acceptMessage.set(acceptMessage);
    }

    /**
     * set a new callback function for future messages and also runs it for each message already in queue
     * @param acceptMessage the new function Connection->Boolean
     */
    public synchronized void bindFunctionAndTestPrevious(Predicate<Connection> acceptMessage) {
        bindFunction(acceptMessage);

        // process the messages received before the call of bindFunction
        int steps = messagesToProcess.size();
        for (int i = 0; i < steps; i++) {  // roll the queue on itself until is back to start
            if(acceptMessage.test(this)) {  // test first element
                messagesToProcess.poll();
            } else {
                messagesToProcess.add(messagesToProcess.poll());
            }
        }
    }

    /**
     * loops until stop() is called and waits for new messages
     */
    protected void listenMessages() {
        String toLog = "Listening for new messages from: " + socket.getInetAddress();
        logger.log(Level.INFO, toLog);
        while (isRunning()) {
            try {
                Message msg = (Message) reader.readObject();
                if (msg instanceof Move move) {
                    updateQueue(move);
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

    /**
     * The queue is used to make sure the moves are processed in the sending order rather than receiving order:
     * they stay in the queue until all the moves sent before are passed to processMessage() and the nthey exit
     * the queue respecting the sending order
     * @param move the move to add to the queue respecting the order given from its number attribute
     */
    void updateQueue(Move move) {
        movesQueue.add(move);
        for (Move m: movesQueue.pollAvailable()) {
            processMessage(m);
        }
    }

    /**
     * tries to consume the new message with the callback function, if not possible adds the message to the
     * messagesToProcess queue
     * @param message the message to process
     */
    private synchronized void processMessage(Message message) {
        String toLog = "Received new object from " + socket.getInetAddress() + ": " + message;
        logger.log(Level.INFO, toLog);
        // put the new element first for the initial test
        List<Message> previousContent = messagesToProcess.stream().toList();
        messagesToProcess.clear();
        messagesToProcess.add(message);
        messagesToProcess.addAll(previousContent);
        if(acceptMessage.test(this)) {
            messagesToProcess.poll();  // remove the only message accessible by acceptMessage
        }
        else {
            messagesToProcess.add(messagesToProcess.poll());  // put in queue the new message
        }
        notifyAll();
    }

    /**
     * checks if the message belongs to one of the classes in the filter
     * @param message the message to be checked
     * @param filter a list of class that the filter will match
     * @return if the message matches
     */
    static boolean matchFileter(Message message, List<Class<?>> filter) {
        for (Class<?> c: filter) {
            if(c.isInstance(message))
                return true;
        }
        return false;
    }

    /**
     * wait for any message, not discriminating at all on the class
     * @return the first received message or in queue to be processed
     */
    public Message waitMessage() {
        return waitMessage(Message.class);
    }

    /**
     * waits for a message belonging to a specific class
     * @param filter the class of the received message
     * @return the first received message or in queue to be processed that matches the class
     */
    public Message waitMessage(Class<?> filter) {
        List<Class<?>> filterList = new ArrayList<>();
        filterList.add(filter);
        return waitMessage(filterList);
    }

    /**
     * waits for a message that matches the filter
     * @param filter a list of class that compose the filter
     * @return the first received message or in queue to be processed that matches the filter
     */
    public synchronized Message waitMessage(List<Class<?>> filter) {
        Message m = pollFirstMatch(filter); // eventual message that was received before the call of waitMessage
        while (m == null) {  // if no compatible found wait for one
            try {
                wait();
                m = pollFirstMatch(filter);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        return m;
    }

    /**
     * remove the first matching message from messagesToProcess queue and returns it
     * @param filter the filter the message must match
     * @return the message removed from the queue or null if none is available
     */
    synchronized Message pollFirstMatch(List<Class<?>> filter) {
        for (Message m: messagesToProcess) {
            if(matchFileter(m, filter)) {
                messagesToProcess.remove(m);
                return m;
            }
        }
        return null;
    }

    /**
     * remove the first message in the messagesToProcess queue and return it
     * @return the removed message
     */
    public synchronized Message getFirstMessage() {
        return messagesToProcess.peek();
    }

    /**
     * serialize the message and send it through the network, if the message is a move set sending order before
     * @param message the message to be serialized and sent
     */
    public void send(Message message) {
        try {
            Move move = (Move) message;
            int next = movesCount.increment();
            move.setNumber(next-1);
        } catch (ClassCastException ignored) { /*ignored*/ }
        try {
            synchronized (writer) {
                writer.writeObject(message);
            }
        } catch (IOException ignored) { /*ignored*/ }
    }

    /**
     * checks if the connection is running by checking socket state
     * @return if the connection is still listening for messages
     */
    boolean isRunning() {
        return !socket.isClosed();
    }

    /**
     * stops the listen message loop by closing the socket that will raise an exception on readObject()
     */
    void stop() {
        try {
            socket.close();
        } catch (IOException ignored) { /*ignored*/ }
        pingTimer.cancel();
    }

    /**
     * runs stop() and wait for the listen message thread to terminate
     */
    public void close() {
        try {
            stop();
            if (!Thread.currentThread().equals(thread))
                thread.join();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * used in test to check that all the messages where processed correctly
     * @return if the messageToProcess queue is empty
     */
    public synchronized boolean noMessageLeft() {
        return messagesToProcess.isEmpty();
    }
}

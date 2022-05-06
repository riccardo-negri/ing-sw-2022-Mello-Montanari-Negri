package it.polimi.ingsw.utils;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.function.Predicate;

public class Connection extends ConnectionBase {
    private final Counter sentCount = new Counter();
    private final Counter receivedCount = new Counter();

    private final Queue<Message> receivedQueue = new PriorityQueue<>((a, b) -> a.getNumber() - b.getNumber());
    private final Queue<MessageContent> messagesToProcess = new LinkedList<>();

    public Connection(SafeSocket socket, Predicate<Connection> acceptMessage) {
        super(socket, acceptMessage);

    }

    public Connection(SafeSocket socket) {
        super(socket, Connection::doNothing);

    }

    public Connection(String address, int port, Predicate<Connection> acceptMessage) {
        super(Connection.createSocket(address, port), acceptMessage);

    }

    public Connection(String address, int port) {
        super(Connection.createSocket(address, port), Connection::doNothing);
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

    protected void listenMessages() {
        System.out.println("Listening for new messages from: " + socket.getInetAddress());
        while (isRunning()) {
            try {
                Message msg = (Message) reader.readObject();
                updateQueue(msg);
            } catch (IOException e) {
                if (e instanceof EOFException) {  // EOF means that the connection was closed from the other end
                    processMessageContent(new Disconnected());
                    return;
                } else if (e instanceof SocketException) {  // SocketException I called close() on this socket
                    return;
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ClassCastException | ClassNotFoundException ignored) {}
        }
    }

    /*
    The queue is used to make sure the messages are processed in the correct order, follows this procedure:
    Add the new message to the queue in the correct position
    Process all the messages in the queue following the order they were sent, remove them when done
    If on message is missing stops processing and will continue from there on the next call
     */
    private synchronized void updateQueue(Message newMessage) {
        receivedQueue.add(newMessage);
        while (receivedQueue.peek() != null && receivedQueue.peek().getNumber() == receivedCount.get()) {
            Message msg = receivedQueue.poll();
            receivedCount.increment();
            if (msg != null)
                processMessage(msg);
        }
    }

    private void processMessage(Message message) {
        processMessageContent(message.getContent());
    }

    private synchronized void processMessageContent(MessageContent message) {
        System.out.println("Received new object from " + socket.getInetAddress() + ": " + message);
        messagesToProcess.add(message);
        if(acceptMessage.test(this)) {
            messagesToProcess.poll();  // remove the only message accessible by acceptMessage
        }
        notifyAll();
    }

    static boolean matchFileter(MessageContent message, List<Class> filter) {
        for (Class c: filter) {
            if(c.isInstance(message))
                return true;
        }
        return false;
    }

    public MessageContent waitMessage() {
        return waitMessage(MessageContent.class);
    }

    public MessageContent waitMessage(Class filter) {
        List<Class> filterList = new ArrayList<>();
        filterList.add(filter);
        return waitMessage(filterList);
    }

    public synchronized MessageContent waitMessage(List<Class> filter) {
        for (MessageContent m: messagesToProcess) {  // if message was received before the call of waitMessage
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

    public synchronized MessageContent getLastMessage () {
        return messagesToProcess.peek();
    }

    public void send(MessageContent message) {
        try {
            int next = sentCount.increment();
            writer.writeObject(new Message(next-1, message));
        } catch (IOException ignored) {}
    }

    boolean isRunning() {
        return !socket.isClosed();
    }

    void stop() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }

    public void close() {
        try {
            stop();
            if (!Thread.currentThread().equals(thread))
                thread.join();
        } catch (InterruptedException ignored) {}
    }
}

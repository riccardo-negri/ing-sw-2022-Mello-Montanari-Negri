package it.polimi.ingsw.utils;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class Connection extends ConnectionBase {
    private MessageContent lastMessage;
    private Integer messageCount = 0;
    private final Object messageCountLock = new Object();

    public Connection(Socket socket, Consumer<Connection> onNewMessage) {
        super(socket, onNewMessage);

    }

    public Connection(Socket socket) {
        super(socket, Connection::doNothing);

    }

    public Connection(String address, int port, Consumer<Connection> onNewMessage) {
        super(Connection.createSocket(address, port), onNewMessage);

    }

    public Connection(String address, int port) {
        super(Connection.createSocket(address, port), Connection::doNothing);
    }

    static private void doNothing(Connection source) {}

    static private Socket createSocket(String address, int port) {
        try {
            return new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void bindFunction(Consumer<Connection> onNewMessage) {
        this.onNewMessage = onNewMessage;
    }

    protected void listenMessages() {
        System.out.println("Listening for new messages from: " + targetAddress);
        while (isRunning()) {
            try {
                Message msg = (Message) reader.readObject();
                System.out.println("Received new object from " + targetAddress + ": " + msg);
                processMessage(msg);
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

    private void processMessage(Message message) {
        processMessageContent(message.getContent());
    }

    private synchronized void processMessageContent(MessageContent message) {
        lastMessage = message;
        onNewMessage.accept(this);
        notifyAll();
    }

    public MessageContent waitMessage() {
        return waitMessage(MessageContent.class);
    }

    public synchronized MessageContent waitMessage(Class filter) {
        while (!filter.isInstance(lastMessage)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return lastMessage;
    }

    public synchronized MessageContent getLastMessage () {
        return lastMessage;
    }

    public void send(MessageContent message) {
        try {
            synchronized (messageCountLock) {
                writer.writeObject(new Message(messageCount, message));
                messageCount++;
            }
        } catch (IOException ignored) {}
    }

    boolean isRunning() {
        synchronized (socket) {
            return !socket.isClosed();
        }
    }

    void stop() {
        try {
            synchronized (socket) {
                socket.close();
            }
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

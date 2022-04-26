package it.polimi.ingsw.utils;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class Connection extends ConnectionBase {
    private Message lastMessage;
    private  final Object lastMessageLock = new Object();

    public Connection(Socket socket, Consumer<ReceivedMessage> onNewMessage) {
        super(socket, onNewMessage);

    }

    public Connection(Socket socket) {
        super(socket, Connection::doNothing);

    }

    public Connection(String address, int port, Consumer<ReceivedMessage> onNewMessage) {
        super(Connection.createSocket(address, port), onNewMessage);

    }

    public Connection(String address, int port) {
        super(Connection.createSocket(address, port), Connection::doNothing);
    }

    static private void doNothing(ReceivedMessage message) {}

    static private Socket createSocket(String address, int port) {
        try {
            return new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void bindFunction(Consumer<ReceivedMessage> onNewMessage) {
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
                    processMessage(new Disconnected());
                    return;
                } else if (e instanceof SocketException) {  // SocketException I called close() on this socket
                    return;
                } else {
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void processMessage(Message message) {
        synchronized (this) {
            onNewMessage.accept(new ReceivedMessage(message, this));
        }
        synchronized (lastMessageLock) {
            lastMessage = message;
            lastMessageLock.notifyAll();
        }
    }

    public Message waitMessage() {
        return waitMessage(Message.class);
    }

    public Message waitMessage(Class filter) {
        synchronized (lastMessageLock) {
            while (!filter.isInstance(lastMessage)) {
                try {
                    lastMessageLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return lastMessage;
        }
    }

    public void send(Message message) {
        try {
            writer.writeObject(message);
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

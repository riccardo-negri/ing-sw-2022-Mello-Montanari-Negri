package it.polimi.ingsw.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        System.out.println("super done");

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
        while (true) {
            try {
                Message msg = (Message) reader.readObject();
                System.out.println("Received new object from " + targetAddress + ": " + msg);
                synchronized (this) {
                    onNewMessage.accept(new ReceivedMessage(msg, this));
                }
                synchronized (lastMessageLock) {
                    lastMessage = msg;
                    lastMessageLock.notifyAll();
                }
            } catch (IOException e) {
                if (e instanceof EOFException || e instanceof SocketException) {
                    return;
                }
                else {
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Message waitMessage() {
        synchronized (lastMessageLock) {
            while (lastMessage == null) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void stop() {
        try {
            socket.close();
        } catch (IOException ignored) {}
    }

    public void close() {
        try {
            stop();
            thread.join();
        } catch (InterruptedException ignored) {}
    }
}

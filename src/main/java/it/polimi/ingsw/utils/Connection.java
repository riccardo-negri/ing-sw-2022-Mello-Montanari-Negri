package it.polimi.ingsw.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class Connection {

    private final Socket socket;
    private final String targetAddress;

    private Consumer<ReceivedMessage> onNewMessage;

    private final Thread thread;

    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;


    public Connection(Socket socket, Consumer<ReceivedMessage> onNewMessage) {
        try {
            this.socket = socket;
            this.targetAddress = String.valueOf(socket.getInetAddress());
            this.onNewMessage = onNewMessage;
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new ObjectOutputStream(socket.getOutputStream());
            thread = new Thread(this::listenMessages);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection(String address, int port, Consumer<ReceivedMessage> onNewMessage) {
        try {
            this.socket = new Socket(address, port);
            this.targetAddress = address;
            this.onNewMessage = onNewMessage;
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            thread = new Thread(this::listenMessages);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void bindFunction(Consumer<ReceivedMessage> onNewMessage) {
        this.onNewMessage = onNewMessage;
    }

    public void listenMessages() {
        System.out.println("Listening for new messages from: " + targetAddress);
        while (true) {
            try {
                Message msg = (Message) reader.readObject();
                System.out.println("Received new object from " + targetAddress + ": " + msg);
                synchronized (this) {
                    onNewMessage.accept(new ReceivedMessage(msg, this));
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

    public void send(Message message) {
        try {
            writer.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
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

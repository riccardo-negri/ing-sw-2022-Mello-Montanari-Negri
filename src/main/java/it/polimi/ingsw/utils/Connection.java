package it.polimi.ingsw.utils;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Connection extends ConnectionBase {
    private Integer messageCount = 0;
    private final Object messageCountLock = new Object();

    public Connection(Socket socket, Predicate<Connection> acceptMessage) {
        super(socket, acceptMessage);

    }

    public Connection(Socket socket) {
        super(socket, Connection::doNothing);

    }

    public Connection(String address, int port, Predicate<Connection> acceptMessage) {
        super(Connection.createSocket(address, port), acceptMessage);

    }

    public Connection(String address, int port) {
        super(Connection.createSocket(address, port), Connection::doNothing);
    }

    static private boolean doNothing(Connection source) {return false;}

    static private Socket createSocket(String address, int port) {
        try {
            return new Socket(address, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void bindFunction(Predicate<Connection> acceptMessage) {
        this.acceptMessage = acceptMessage;
    }

    protected void listenMessages() {
        System.out.println("Listening for new messages from: " + targetAddress);
        while (isRunning()) {
            try {
                Message msg = (Message) reader.readObject();
                System.out.println("Received new object from " + targetAddress + ": " + msg.getContent());
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
        messages.add(message);
        if(acceptMessage.test(this)) {
            removeLastMessage();  // remove the only message accessible by acceptMessage
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
        for (MessageContent m: messages) {  // if message was received before the call of waitMessage
            if(matchFileter(m, filter)) {
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
        MessageContent result = getLastMessage();
        removeLastMessage();
        return result;
    }

    public synchronized MessageContent getLastMessage () {
        if (messages.size() > 0) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }

    private synchronized void removeLastMessage() {
        if (messages.size() > 0) {
            messages.remove(messages.size() - 1);
        }
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

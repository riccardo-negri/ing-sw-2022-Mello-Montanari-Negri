package it.polimi.ingsw.utils;

import java.net.Socket;
import java.util.function.Consumer;

public class ReplacingConnection extends Connection{
    private final Connection oldConnection;

    public ReplacingConnection(String address, int port, Consumer<ReceivedMessage> onNewMessage, Connection old) {
        super(address, port, onNewMessage);
        oldConnection = old;
    }

    public void listenMessages() {
        oldConnection.close();
        super.listenMessages();
    }
}

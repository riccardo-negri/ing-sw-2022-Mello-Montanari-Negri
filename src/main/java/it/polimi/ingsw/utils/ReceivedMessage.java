package it.polimi.ingsw.utils;

public class ReceivedMessage {
    private final Message content;
    private final Connection source;

    public ReceivedMessage(Message content, Connection source) {
        this.content = content;
        this.source = source;
    }

    public Connection getSource() {
        return source;
    }

    public Message getContent() {
        return content;
    }
}

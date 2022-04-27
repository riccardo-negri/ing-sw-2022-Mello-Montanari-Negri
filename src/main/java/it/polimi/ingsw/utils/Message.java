package it.polimi.ingsw.utils;

import java.io.Serializable;

public class Message implements Serializable {
    private final int number;
    private final MessageContent content;

    public Message(int number, MessageContent content) {
        this.number = number;
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public MessageContent getContent() {
        return content;
    }
}

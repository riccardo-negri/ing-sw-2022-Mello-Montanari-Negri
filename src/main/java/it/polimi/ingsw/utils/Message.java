package it.polimi.ingsw.utils;

import java.util.Optional;

public abstract class Message {
    protected Optional<Connection> source;

    public Optional<Connection> getSource() {
        return source;
    }

    public void setSource(Connection source) {
        this.source = Optional.of(source);
    }

    public void removeSource() {
        this.source = Optional.empty();
    }
}

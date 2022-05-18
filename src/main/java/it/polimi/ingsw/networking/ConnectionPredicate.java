package it.polimi.ingsw.networking;

import java.util.function.Predicate;

/*
    A thread-safe class that contains a Connection Predicate
 */
public class ConnectionPredicate {
    private Predicate<Connection> predicate;

    public ConnectionPredicate(Predicate<Connection> predicate) {
        this.predicate = predicate;
    }

    public synchronized void set(Predicate<Connection> predicate) {
        this.predicate = predicate;
    }

    public synchronized boolean test(Connection connection) {
        return predicate.test(connection);
    }

}

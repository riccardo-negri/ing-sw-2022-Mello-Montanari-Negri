package it.polimi.ingsw.networking;

import java.util.function.Predicate;


/**
 * A thread-safe class both on read and write that contains a Connection Predicate
 */
public class ConnectionPredicate {
    private Predicate<Connection> predicate;

    /**
     * creates a connection predicate object
     * @param predicate the initial predicate contained in this class
     */
    public ConnectionPredicate(Predicate<Connection> predicate) {
        this.predicate = predicate;
    }

    /**
     * changes the predicate contained in this class
     * @param predicate the new predicate to contain
     */
    public synchronized void set(Predicate<Connection> predicate) {
        this.predicate = predicate;
    }

    /**
     * uses the current predicate contained to run a test
     * @param connection the connection to be tested
     * @return the result of the test
     */
    public synchronized boolean test(Connection connection) {
        return predicate.test(connection);
    }

}

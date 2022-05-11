package it.polimi.ingsw.server;

import java.util.Vector;

/*
    A thread-safe list that also has a limitate capacity and refuses duplicates
 */

public class CapacityVector<E> extends Vector<E> {
    private final int capacity;

    public CapacityVector(int capacity) {
        super();
        this.capacity = capacity;
    }

    @Override
    public synchronized boolean add(E e) {
        if (size() < capacity && !contains(e)) {
            return super.add(e);
        }
        return false;
    }
}

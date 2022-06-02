package it.polimi.ingsw.server;

import java.util.Collection;

/*
    A thread-safe list that also has a limitate capacity and refuses duplicates
 */

public class CapacityList<E> extends SafeList<E> {
    private final int capacity;

    public CapacityList(int capacity) {
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

    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            if(!add(e))
                return false;
        }
        return true;
    }
}

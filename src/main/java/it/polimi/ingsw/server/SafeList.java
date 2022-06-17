package it.polimi.ingsw.server;

import java.util.ArrayList;

// Thread-safe list

public class SafeList<E> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        synchronized (this) {
            return super.add(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (this) {
            return super.remove(o);
        }
    }

    @Override
    public E remove(int index) {
        synchronized (this) {
            return super.remove(index);
        }
    }

    @Override
    public int indexOf(Object o) {
        synchronized (this) {
            return super.indexOf(o);
        }
    }
}

package it.polimi.ingsw.server;

import java.util.ArrayList;

// Thread-safe list

public class SafeList<E> extends ArrayList<E> {

    /**
     * add an element to the end of the list
     * @param e the element to add
     * @return if the element was added successfully
     */
    @Override
    public boolean add(E e) {
        synchronized (this) {
            return super.add(e);
        }
    }

    /**
     * remove the specified element from the list
     * @param o the element to remove
     * @return if the element was remove successfully
     */
    @Override
    public boolean remove(Object o) {
        synchronized (this) {
            return super.remove(o);
        }
    }

    /**
     * remove the element at the specified position from the list
     * @param index the position of the element to remove
     * @return if the element was remove successfully
     */
    @Override
    public E remove(int index) {
        synchronized (this) {
            return super.remove(index);
        }
    }

    /**
     * find the position of an element in the list
     * @param o the element to search in the list
     * @return the position of the specified element or -1 if not present
     */
    @Override
    public int indexOf(Object o) {
        synchronized (this) {
            return super.indexOf(o);
        }
    }
}

package it.polimi.ingsw.server;

import java.util.Collection;

/*
    A thread-safe list that also has a limitate capacity and refuses duplicates
 */

public class CapacityList<E> extends SafeList<E> {
    private final int capacity;

    /**
     * create a list with limitate capacity
     * @param capacity the maximum number of element that this list can contain
     */
    public CapacityList(int capacity) {
        super();
        this.capacity = capacity;
    }

    /**
     * if there is room left in the list add the new element at the end
     * @param e the element to add
     * @return if the element was added
     */
    @Override
    public synchronized boolean add(E e) {
        if (size() < capacity && !contains(e)) {
            return super.add(e);
        }
        return false;
    }

    /**
     * add to the list all the elements in c until there is room left
     * @param c the collection of the element to add
     * @return if all the elements were added
     */
    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            if(!add(e))
                return false;
        }
        return true;
    }
}

package it.polimi.ingsw.utils;

import java.io.Serializable;

public class Counter implements Serializable {
    private int number = 0;

    /**
     * create a counter initialized at 0
     */
    public Counter() {}

    /**
     * create a counter initialized at the given value
     * @param start the initial value of the counter number
     */
    public Counter(int start) {
        number = start;
    }

    /**
     * edits the internal variable and returns the new value
     * @return internal value after the increment
     */
    public synchronized int increment() {
        number++;
        return number;
    }

    /**
     * get number value
     * @return the internal value of the counter
     */
    public synchronized int get() {
        return number;
    }

}

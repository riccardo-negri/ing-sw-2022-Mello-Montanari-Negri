package it.polimi.ingsw.utils;

import java.io.Serializable;

public class Counter implements Serializable {
    private int number = 0;

    public Counter() {}

    public Counter(int start) {
        number = start;
    }

    // edits the internal variable and returns the new value
    public synchronized int increment() {
        number++;
        return number;
    }

    public synchronized int get() {
        return number;
    }

}

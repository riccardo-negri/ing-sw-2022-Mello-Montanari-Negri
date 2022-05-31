package it.polimi.ingsw.utils;

public class Counter {
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

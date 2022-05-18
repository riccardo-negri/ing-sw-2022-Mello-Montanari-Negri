package it.polimi.ingsw.networking;

public class Counter {
    private int number = 0;

    // edits the internal variable and returns the new value
    public synchronized int increment() {
        number++;
        return number;
    }

    public synchronized int get() {
        return number;
    }

}

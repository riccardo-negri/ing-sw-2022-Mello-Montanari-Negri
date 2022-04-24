package it.polimi.ingsw.utils;

public abstract class Move extends Message{
    private int number = 0;

    void applyEffect(/*Model*/) {

    }

    public void setNumber(int number) {
        this.number = number;
    }
}

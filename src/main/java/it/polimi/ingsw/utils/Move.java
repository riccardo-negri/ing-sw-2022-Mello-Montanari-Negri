package it.polimi.ingsw.utils;

public abstract class Move extends Message{
    private int number = 0;

    public abstract void applyEffect(/*Model*/);
    public abstract void validate(/*Model*/) throws Exception;

    public void setNumber(int number) {
        this.number = number;
    }
}

package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.Wizard;

public abstract class Move extends Message{
    private int number = 0;

    private Wizard wizard;

    public abstract void applyEffect(/*Model*/);
    public abstract void validate(/*Model*/) throws Exception;

    public void setNumber(int number) {
        this.number = number;
    }
}

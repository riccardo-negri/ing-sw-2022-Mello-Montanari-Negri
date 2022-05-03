package it.polimi.ingsw.utils;

public class InitialState extends MessageContent {
    private final String state;
    public InitialState(String state) {this.state = state;}

    public String getState() {
        return state;
    }
}

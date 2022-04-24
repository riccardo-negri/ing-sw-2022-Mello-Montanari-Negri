package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Message;
import it.polimi.ingsw.utils.Move;
import it.polimi.ingsw.utils.ReceivedMessage;

import java.util.ArrayList;
import java.util.List;

public class GameServer extends Server{
    private final boolean advancedRules;
    private final List<String> assignedUsernames;
    private Integer moveCount = 0;
    private final Object moveCountLock;

    public GameServer(int size, boolean advancedRules) {
        maxUsers = size;
        this.advancedRules = advancedRules;
        this.assignedUsernames = new ArrayList<>();
        this.moveCountLock = new Object();
    }

    @Override
    void onStart() {

    }

    @Override
    void onQuit() {

    }

    void receiveMessage(ReceivedMessage received) {
        Message content = received.getContent();
        if (content instanceof Move) {
            echoMove((Move) content);
        } else {
            throw new RuntimeException("Unknown message");
        }
    }

    void echoMove(Move move) {
        synchronized (moveCountLock) {
            moveCount++;
            move.setNumber(moveCount);
        }
        for (User user : getConnectedUser()) {
            user.getConnection().send(move);
        }
    }

    @Override
    void onUserReconnected(User user) {
        user.getConnection().bindFunction(this::receiveMessage);
    }

    @Override
    void onNewUserConnect(User user, Login info) {
        user.getConnection().bindFunction(this::receiveMessage);
    }

    public boolean isFull() {
        return assignedUsernames.size() >= maxUsers;
    }

    public void assignUser(String name) {
        if (!isFull()) {
            assignedUsernames.add(name);
        }
    }

    public List<String> getAssignedUsernames() {
        return assignedUsernames;
    }


    public boolean isAdvancedRules() {
        return advancedRules;
    }
}

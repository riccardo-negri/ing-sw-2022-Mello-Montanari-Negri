package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.*;

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
        Connection source = received.getSource();
        if (content instanceof Disconnected) {
            User user = userFromConnection(source);
            if (user != null)
                broadcast(new UserDisconnected(user.getName()));
        } else if (content instanceof Move) {
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
        broadcast(move);
    }

    void broadcast(Message message) {
        for (User user : getConnectedUser()) {
            user.getConnection().send(message);
        }
    }

    @Override
    void onUserReconnected(User user) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isFull()) {
            user.getConnection().send(new GameStart());
        }
    }

    @Override
    void onNewUserConnect(User user, Login info) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isFull()) {
            broadcast(new GameStart());
        }
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

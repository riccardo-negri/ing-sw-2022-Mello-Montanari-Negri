package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.utils.moves.InitialState;
import it.polimi.ingsw.utils.moves.Move;

import java.util.ArrayList;
import java.util.List;

public class GameServer extends Server{
    private final boolean advancedRules;
    private final List<String> assignedUsernames;

    public GameServer(int size, boolean advancedRules) {
        maxUsers = size;
        this.advancedRules = advancedRules;
        this.assignedUsernames = new ArrayList<>();
    }

    @Override
    void onStart() {

    }

    @Override
    void onQuit() {

    }

    void receiveMessage(Connection source) {
        MessageContent message = source.getLastMessage();
        if (message instanceof Disconnected) {
            User user = userFromConnection(source);
            if (user != null)
                broadcast(new UserDisconnected(user.getName()));
        } else if (message instanceof Move) {
            ((Move) message).applyEffect();
            broadcast(message);
        } else {
            throw new RuntimeException("Unknown message");
        }
    }

    void broadcast(MessageContent message) {
        for (User user : getConnectedUser()) {
            user.getConnection().send(message);
        }
    }

    @Override
    void onUserReconnected(User user) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isFull()) {
            user.getConnection().send(new InitialState());
        }
    }

    @Override
    void onNewUserConnect(User user, Login info) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isFull()) {
            broadcast(new InitialState());
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

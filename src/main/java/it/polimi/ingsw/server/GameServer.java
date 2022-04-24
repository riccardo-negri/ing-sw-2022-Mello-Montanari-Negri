package it.polimi.ingsw.server;

import it.polimi.ingsw.utils.Login;
import it.polimi.ingsw.utils.Message;

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

    void receiveMessage(Message message) {

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

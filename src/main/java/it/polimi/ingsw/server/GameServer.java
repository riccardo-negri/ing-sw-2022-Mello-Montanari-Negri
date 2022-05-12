package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.utils.*;
import it.polimi.ingsw.utils.InitialState;
import it.polimi.ingsw.utils.moves.Move;

import java.util.List;

public class GameServer extends Server{
    private final CapacityVector<String> assignedUsernames;

    private final Game game;

    public GameServer(PlayerNumber playerNumber, GameMode mode) {
        maxUsers = playerNumber.getWizardNumber();
        this.assignedUsernames = new CapacityVector<>(maxUsers);
        int id = Game.gameEntityFactory(mode, playerNumber);
        game = Game.request(id);
    }

    @Override
    void onStart() {

    }

    @Override
    void onQuit() {

    }

    boolean receiveMessage(Connection source) {
        Message message = source.getLastMessage();
        if (message instanceof Disconnected) {
            User user = userFromConnection(source);
            if (user != null)
                broadcast(new UserDisconnected(user.getName()));
        } else if (message instanceof Move) {
            doMove((Move) message, source);
        } else {
            throw new RuntimeException("Unknown message" + message);
        }
        return true;
    }

    void doMove(Move move, Connection source) {
        Wizard wizard = ((GameUser) userFromConnection(source)).getWizard();
        try {
            move.applyEffectServer(game, wizard);
            broadcast(move);
        } catch (Exception ignored) {}
    }

    void broadcast(Message message) {
        for (User user : getConnectedUser()) {
            user.getConnection().send(message);
        }
    }

    @Override
    void onUserReconnected(User user) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isEveryoneConnected()) {
            user.getConnection().send(new InitialState(game.serializeGame(), usernames()));
        }
    }

    @Override
    void onNewUserConnect(User user, Login info) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isEveryoneConnected()) {
            broadcast(new InitialState(game.serializeGame(), usernames()));
        }
    }

    @Override
    boolean isUserAllowed(Login info) {
        return assignedUsernames.contains(info.getUsername());
    }

    @Override
    User createUser(String name, Connection connection) {
        int id;
        id = connectedUser.size();
        return new GameUser(name, connection, game.getWizard(id));
    }

    public boolean assignUser(String name) {
        return assignedUsernames.add(name);
    }

    public List<String> getAssignedUsernames() {
        return assignedUsernames;
    }

    public GameMode getGameMode() {
        return game.getGameMode();
    }

    public PlayerNumber getPlayerNumber() {return game.getPlayerNumber();}
}

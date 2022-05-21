package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.moves.Move;

import java.util.List;
import java.util.logging.Level;

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
        GameUser user = userFromConnection(source);
        if (message instanceof Disconnected) {
            user.setDisconnected(true);
            broadcast(new UserDisconnected(user.getName()));
        } else if (message instanceof UserResigned resign) {
            resign.setUsername(user.name);
            broadcast(resign);
        } else if (message instanceof Move move) {
            doMove(move, source);
        } else {
            throw new RuntimeException("Unknown message" + message);
        }
        return true;
    }

    void doMove(Move move, Connection source) {
        Wizard wizard = userFromConnection(source).getWizard();
        try {
            move.applyEffectServer(game, wizard);
            broadcast(move);
            if (game.isGameEnded()) {
                stop();
            }
        } catch (Exception e) {
            String toLog = "Move refused: " + e.getMessage();
            logger.log(Level.WARNING, toLog);
        }
    }

    void broadcast(Message message) {
        for (User user : getConnectedUsers()) {
            user.getConnection().send(message);
        }
    }

    @Override
    void onUserReconnected(User user) {
        user.getConnection().bindFunction(this::receiveMessage);
        ((GameUser) user).setDisconnected(false);
        if (isEveryoneConnected()) {
            // In the game server means that everyone joined once, but we don't know if the connection was lost
            user.getConnection().send(new InitialState(game.serializeGame(), usernames()));
            tellWhoIsDisconnected(user);
            broadcast(new UserReconnected(user.name));
        }
    }

    @Override
    void onNewUserConnect(User user, Login info) {
        user.getConnection().bindFunction(this::receiveMessage);
        if (isEveryoneConnected()) {
            // Game is starting
            broadcast(new InitialState(game.serializeGame(), usernames()));
            for (User u: connectedUsers)
                tellWhoIsDisconnected(u);
        }
    }

    void tellWhoIsDisconnected(User target) {
        for (User u: connectedUsers) {
            if(((GameUser) u).isDisconnected())
                target.getConnection().send(new UserDisconnected(u.name));
        }
    }

    @Override
    boolean isUserAllowed(Login info) {
        return assignedUsernames.contains(info.getUsername());
    }

    @Override
    User createUser(String name, Connection connection) {
        int id;
        id = connectedUsers.size();
        return new GameUser(name, connection, game.getWizard(id));
    }

    public boolean assignUser(String name) {
        return assignedUsernames.add(name);
    }

    public List<String> getAssignedUsernames() {
        return assignedUsernames;
    }

    @Override
    public GameUser userFromConnection(Connection connection) {
        return (GameUser) super.userFromConnection(connection);
    }

    public GameMode getGameMode() {
        return game.getGameMode();
    }

    public PlayerNumber getPlayerNumber() {return game.getPlayerNumber();}
}

package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.InitialState;
import it.polimi.ingsw.networking.moves.Move;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class GameServer extends Server{
    private final CapacityVector<String> assignedUsernames;

    private final Game game;

    private Timer afkTimer;
    private static final int afkPeriod = 300;  // 5 minutes

    private final GameSavesManager savesManager;

    public GameServer(Game game, List<String> usernames, GameSavesManager savesManager) {
        maxUsers = game.getPlayerNumber().getWizardNumber();
        this.assignedUsernames = new CapacityVector<>(maxUsers);
        assignedUsernames.addAll(usernames);
        this.game = game;
        this.savesManager = savesManager;
    }

    public GameServer(PlayerNumber playerNumber, GameMode mode, GameSavesManager savesManager) {
        maxUsers = playerNumber.getWizardNumber();
        this.assignedUsernames = new CapacityVector<>(maxUsers);
        int id = Game.gameEntityFactory(mode, playerNumber);
        game = Game.request(id);
        this.savesManager = savesManager;
    }

    @Override
    void onStart() {

    }

    @Override
    void onQuit() {
        if (afkTimer != null)
            afkTimer.cancel();
        savesManager.deleteGameFolder();
        try {
            game.delete();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    boolean receiveMessage(Connection source) {
        Message message = source.getLastMessage();
        GameUser user = userFromConnection(source);
        if (message instanceof Disconnected) {
            user.setDisconnected(true);
            broadcast(new UserDisconnected(user.getName()));
        } else if (message instanceof UserResigned resign) {
            resign.setUsername(user.name);  // can't trust client
            broadcast(resign);
            stop();
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
            afkTimer.cancel(); // terminates any previous scheduled task
            setAfkTimer();
            broadcast(move);
            savesManager.createSnapshot(game);
            if (game.isGameEnded()) {
                stop();
            }
        } catch (Exception e) {
            String toLog = "Move refused: " + e.getMessage();
            logger.log(Level.WARNING, toLog);
        }
    }


    void setAfkTimer() {
        afkTimer = new Timer();
        TimerTask afkTask = new TimerTask() {
            public void run() {
                broadcast(new UserResigned(userCurrentlyPlaying().getName()));
                stop();
            }
        };
        afkTimer.schedule(afkTask, afkPeriod * 1000);
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
            setAfkTimer();
            if(!savesManager.createGameFolder(game, assignedUsernames)) {
                stop();
            }
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

    User userCurrentlyPlaying() {
        int id = game.getGameState().getCurrentPlayer();
        User result = null;
        for (User u: connectedUsers) {
            if (((GameUser) u).getWizard().getId() == id) {
                result = u;
            }
        }
        return result;
    }

    public String getCode() {
        return savesManager.getCode();
    }
}

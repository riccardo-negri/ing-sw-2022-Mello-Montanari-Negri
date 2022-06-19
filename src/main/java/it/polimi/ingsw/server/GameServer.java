package it.polimi.ingsw.server;

import it.polimi.ingsw.model.entity.Game;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.moves.Move;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class GameServer extends Server{
    private final CapacityList<String> assignedUsernames;

    private final Game game;

    private Timer afkTimer;
    private static final int AFK_PERIOD = 300;  // 5 minutes

    private final GameSavesManager savesManager;

    /**
     * create a game server restored from a save folder
     * @param game the game deserialized from the snapshot
     * @param usernames the username list read form the usernames file
     * @param savesManager the save manager operating on this game save folder
     */
    public GameServer(Game game, List<String> usernames, GameSavesManager savesManager) {
        maxUsers = game.getPlayerNumber().getWizardNumber();
        this.assignedUsernames = new CapacityList<>(maxUsers);
        assignedUsernames.addAll(usernames);
        this.game = game;
        this.savesManager = savesManager;
    }

    /**
     * create a new game server form given parameters
     * @param playerNumber the number of players that will play on this game server
     * @param mode the game mode of the game to create
     * @param savesManager the save manager operating on this game save folder
     */
    public GameServer(PlayerNumber playerNumber, GameMode mode, GameSavesManager savesManager) {
        maxUsers = playerNumber.getWizardNumber();
        this.assignedUsernames = new CapacityList<>(maxUsers);
        int id = Game.gameEntityFactory(mode, playerNumber);
        game = Game.request(id);
        this.savesManager = savesManager;
    }

    /**
     * implementation of the abstract method
     */
    @Override
    void onStart() {
        // no start actions for the game server
    }

    /**
     * stop timer, delete game folder and game instance before terminating
     */
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

    /**
     * callback function to process incoming client messages
     * if user disconnects or resign send the information to everyone, if is a move call doMove()
     * @param source the connection from which the message is coming
     * @return if the message is processed and therefore should be consumed
     */
    boolean receiveMessage(Connection source) {
        Message message = source.getFirstMessage();
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
        }
        return true;
    }

    /**
     * apply the move and send it to everyone if valid
     * @param move the move that was requested
     * @param source the connection from which the message is coming
     */
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

    /**
     * set a timeout for client that disconnects due to inactivity
     * if already exists restart it from the beginning
     */
    void setAfkTimer() {
        afkTimer = new Timer();
        TimerTask afkTask = new TimerTask() {
            public void run() {
                broadcast(new UserResigned(userCurrentlyPlaying().getName()));
                stop();
            }
        };
        afkTimer.schedule(afkTask, (long) AFK_PERIOD * 1000);
    }


    /**
     * send message to alla connected clients
     * @param message the message to send
     */
    void broadcast(Message message) {
        for (User user : getConnectedUsers()) {
            user.getConnection().send(message);
        }
    }

    /**
     * check that the user lost connection before accepting a reconnection
     * @param user the user who reconnected
     */
    @Override
    void onUserReconnected(User user) {
        for (User u: connectedUsers) {  // if user is already connected properly refuse new connection
            GameUser gu = (GameUser) u;
            if (gu.getName().equals(user.getName()) && !gu.isDisconnected()) {
                // don't send error message because only malevolent client can reach this point
                user.getConnection().close(); // don't remove the user, just close the connection
                return;
            }
        }
        user.getConnection().bindFunction(this::receiveMessage);
        ((GameUser) user).setDisconnected(false);
        if (isEveryoneConnected()) {
            // In the game server means that everyone joined once, but we don't know if the connection was lost
            user.getConnection().send(new InitialState(game.serializeGame(), usernames()));
            tellWhoIsDisconnected(user);
            broadcast(new UserConnected(user.name));
        }
    }

    /**
     * add new user and if game is full start the game
     * @param user the user who connected
     */
    @Override
    void onNewUserConnect(User user) {
        user.getConnection().bindFunction(this::receiveMessage);
        for (User u: connectedUsers)  // tell to the new user all the other connected in the lobby
            if (!u.getName().equals(user.getName()))
                user.getConnection().send(new UserConnected(u.name));
        broadcast(new UserConnected(user.name));  // tell to the other in the lobby that the new user is joining
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

    /**
     * send to a target user a UserDisconnected message for any other user currently disconnected
     * @param target the destination user for the messages
     */
    void tellWhoIsDisconnected(User target) {
        for (User u: connectedUsers) {
            if(((GameUser) u).isDisconnected())
                target.getConnection().send(new UserDisconnected(u.name));
        }
    }

    /**
     * a user is allowed if the matchmaking serve assigned its username to this game server
     * @param info login contains the username sent from the client which is available before accepting the user
     * @return if the user is allowed
     */
    @Override
    boolean isUserAllowed(Login info) {
        return assignedUsernames.contains(info.username());
    }

    /**
     * create a game user taking the corresponding wizard from game
     * @param name the username of the user
     * @param connection the connection to the user client
     * @return the new game user
     */
    @Override
    User createUser(String name, Connection connection) {
        int id;
        id = connectedUsers.size();
        return new GameUser(name, connection, game.getWizard(id));
    }

    /**
     * add a username to the assignedUsernames list
     * @param name the username to be assigned to this server
     * @return if the username was assigned successfully
     */
    public boolean assignUser(String name) {
        return assignedUsernames.add(name);
    }

    /**
     * get assignedUsernames value
     * @return the list of usernames assigned to this server from the matchmaking server
     */
    public List<String> getAssignedUsernames() {
        return assignedUsernames;
    }

    /**
     * given the connection find the game user among all the connected game users
     * @param connection the connection belonging to the user
     * @return the game user containing the given connection, null if not present, should always be present
     */
    @Override
    public GameUser userFromConnection(Connection connection) {
        return (GameUser) super.userFromConnection(connection);
    }

    /**
     * get getGameMode() value
     * @return the game mode of this game server
     */
    public GameMode getGameMode() {
        return game.getGameMode();
    }

    /**
     * get getPlayerNumber() value
     * @return the maximum number of players on this server
     */
    public PlayerNumber getPlayerNumber() {return game.getPlayerNumber();}

    /**
     * find the user that is currently playing in the game
     * @return in the game is currently the turn of this user
     */
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

    /**
     * get savesManager code value
     * @return the code associated to this game server save folder
     */
    public String getCode() {
        return savesManager.getCode();
    }
}

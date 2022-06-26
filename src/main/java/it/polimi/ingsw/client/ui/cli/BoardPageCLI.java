package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.moves.Move;
import org.jline.reader.UserInterruptException;
import sun.misc.Signal;

import java.util.*;
import java.util.logging.Level;

import static it.polimi.ingsw.client.ui.cli.utils.BoardUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.ReadMoveUtilsCLI.*;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageCLI extends AbstractBoardPage {
    private String lastWarning;
    private Integer lastHelper;
    static final List<String> moveFromStdin = new ArrayList<>();

    public BoardPageCLI (Client client) {
        super(client);
    }

    /**
     * draw the page and handle all expected actions
     */
    @Override
    public void draw () {
        Signal.handle(new Signal("INT"),  // SIGINT if the player wants to quit the game
                signal -> onQuit(true));

        while (!client.getModel().isGameEnded() && client.getNextState() == ClientPage.BOARD_PAGE) {
            drawGameAndConsole();
            printWarningOrHelper();
            logger.log(Level.INFO, "Drew game board and console area");

            if (model.getGameState().getCurrentPlayer() == client.getUsernames().indexOf(client.getUsername())) { // enter if it's your turn
                moveFromStdin.clear();
                waitForMoveOrMessage();
                if (!moveFromStdin.isEmpty()) { // a move has have been read
                    processMoveFromInput();
                }
                else { // no move was made, so it's a message about some disconnection events
                    Message message = client.getConnection().waitMessage();
                    handleMessageNotMoveAndPrintStatus(message);
                }
            }
            else { // enter here if it's not your turn
                waitAndHandleMessage();
            }
        }

        logger.log(Level.FINE, "Game finished because someone won or surrendered");
        if (client.getNextState() == ClientPage.BOARD_PAGE) onEnd(false);
    }

    /**
     * validate move against current game state and then send it to the server, overrides abstract page method to also apply it to the model
     * @param moveToSend move to be validated and then sent
     * @throws Exception error message if the move is not valid
     */
    @Override
    protected void validateAndSendMove (Move moveToSend) throws Exception{
        super.validateAndSendMove(moveToSend);

        // wait and apply message if the suer is using a CLI
        String toLog = "Waiting for message to come back. Move:" + moveToSend;
        logger.log(Level.INFO, toLog);
        Message message = client.getConnection().waitMessage(Arrays.asList(Move.class, Disconnected.class, UserResigned.class));

        if (message instanceof Disconnected) {
            printConsoleWarning(terminal, "You got disconnected from the game, rejoin the game with the same username. Press enter to go back to the connection page...");
            waitEnterPressed(terminal);
            onEnd(true);
            return;
        }

        else if (message instanceof UserResigned userResigned) {
            printConsoleWarning(terminal, "Player " + userResigned.getUsername() + " resigned from the game. Press enter to go back to the menu...");
            waitEnterPressed(terminal);
            onQuit(false);
            return;
        }

        Move moveToApply = (Move) message;

        toLog = "Applying effects of message. Move:" + moveToSend;
        logger.log(Level.INFO, toLog);
        moveToApply.applyEffectClient(client.getModel());

        toLog = "Applied effect of message. Move:" + moveToSend;
        logger.log(Level.INFO, toLog);
    }

    /**
     * based on current game state ask user for commands with autocompletion
     */
    private void askForMoveBasedOnState () {
        switch (model.getGameState().getGameStateName()) {
            case "PS" ->
                    getMovePlayAssistant(terminal, commandsHistory, client.getUsername(), getCharactersID(model), moveFromStdin);
            case "MSS" ->
                    getMoveStudentToIsland(terminal, commandsHistory, client.getUsername(), getCharactersID(model), moveFromStdin);
            case "MMNS" ->
                    getMoveMotherNature(terminal, commandsHistory, client.getUsername(), getCharactersID(model), moveFromStdin);
            case "CCS" ->
                    getMoveSelectCloud(terminal, commandsHistory, client.getUsername(), getCharactersID(model), moveFromStdin);
            default -> logger.log(Level.WARNING, "Current state is not supported");
        }
    }

    /**
     * handle a message that has been received that is not a move
     * @param message received message
     */
    private void handleMessageNotMoveAndPrintStatus (Message message) {
        final String PLAYER = "Player ";
        if (message instanceof Disconnected) {
            printConsoleWarning(terminal, "You got disconnected from the game, rejoin the game with the same username. Press enter to go back to the connection page...");
            waitEnterPressed(terminal);
            onEnd(true);
        }
        else if (message instanceof UserDisconnected userDisconnected) {
            lastWarning = PLAYER + userDisconnected.username() + " disconnected from the game.";
            client.getUsernamesDisconnected().add(((UserDisconnected) message).username());
        }
        else if (message instanceof UserConnected userConnected) {
            lastWarning = PLAYER + userConnected.username() + " reconnected to the game.";
            client.getUsernamesDisconnected().remove(((UserConnected) message).username());
        }
        else if (message instanceof UserResigned userResigned) {
            if (userResigned.getUsername().equals(client.getUsername())) {
                printConsoleWarning(terminal, "You resigned from the game. Press enter to go back to the menu...");
            }
            else {
                printConsoleWarning(terminal, PLAYER + userResigned.getUsername() + " resigned from the game. Press enter to go back to the menu...");
            }
            waitEnterPressed(terminal);
            onQuit(false);
        }
        else {
            lastWarning = "Received an unsupported message...";
        }

    }

    /**
     * return when a message has been received or when the user typed a valid command
     */
    private void waitForMoveOrMessage () {
        final boolean[] isOriginatedByMessage = {false};
        Thread t = new Thread(() -> {
            try {
                askForMoveBasedOnState();
            } catch (UserInterruptException e) {
                logger.log(Level.SEVERE, e.toString());
                if (!isOriginatedByMessage[0]) {
                    onQuit(true);
                }
            }
        });
        t.start();

        client.getConnection().bindFunctionAndTestPrevious(
                (Connection c) -> {
                    isOriginatedByMessage[0] = true;
                    t.interrupt();
                    return false;
                }
        );

        try {
            t.join();
        } catch (InterruptedException e) {
            t.interrupt();
        }
    }

    /**
     * method to be called when it not your turn, waits for any message and handles it
     */
    private void waitAndHandleMessage () {
        printConsoleInfo(terminal, "Waiting for the other players to do a move...\n");
        Message message = client.getConnection().waitMessage();
        if (message instanceof Move move) {
            try {
                applyOtherPlayersMove(move);
            } catch (Exception e) {
                String toLog = "Got an invalid move from the server. Exception: " + e;
                logger.log(Level.WARNING, toLog);
                e.printStackTrace();
                waitEnterPressed(terminal);
            }
        }
        else {
            handleMessageNotMoveAndPrintStatus(message);
        }
    }

    /**
     * parse move that passed the regex test and call the method to send it to the server
     */
    private void processMoveFromInput () {
        String toLog = "Processing input move: " + moveFromStdin + " Game state: " + model.getGameState().getGameStateName();
        logger.log(Level.INFO, toLog);
        try {
            if (moveFromStdin.get(0).contains("use-character")) {
                parseAndDoCharacterMove(moveFromStdin.get(0));
            }
            else if (moveFromStdin.get(0).contains("character-info")) {
                lastHelper = Integer.parseInt(moveFromStdin.get(0).split("-")[2].strip());
            }
            else {
                switch (model.getGameState().getGameStateName()) {
                    case "PS" -> doCardChoice(Integer.parseInt(moveFromStdin.get(0).split(" ")[1]));
                    case "MSS" ->
                            doStudentMovement(parseStudentColorFromString(moveFromStdin.get(0).split(" ")[1]), moveFromStdin.get(0).split(" ")[3]);
                    case "MMNS" -> doMotherNatureMovement(Integer.parseInt(moveFromStdin.get(0).split(" ")[2]));
                    case "CCS" -> doCloudChoice(Integer.parseInt(moveFromStdin.get(0).split(" ")[1]));
                    default -> logger.log(Level.WARNING, "Current state is not supported");
                }
            }
        } catch (Exception e) {
            toLog = "Got an invalid move (that passed regex check), asking for it again. Exception: " + e;
            logger.log(Level.WARNING, toLog);
            lastWarning = "Please type a valid command. " + e.getMessage();
        }
    }

    /**
     * parse character move that passed regex check and call the method to send it to the server
     * @param move move written by the user
     * @throws Exception error message if the move is not valid
     */
    private void parseAndDoCharacterMove (String move) throws Exception {
        int id = Integer.parseInt(move.split(" ")[0].split("-")[2]);
        ArrayList<Object> parameters = new ArrayList<>();
        final String nothing = "nothing";

        switch (move.split(" ")[0]) { // adding parameters only where needed
            case "use-character-1" -> {
                parameters.add(parseStudentColorFromString(move.split(" ")[2]));
                parameters.add(Integer.parseInt(move.split(" ")[4].split("-")[1]));
            }
            case "use-character-3", "use-character-5" ->
                    parameters.add(Integer.parseInt(move.split(" ")[2].split("-")[1]));
            case "use-character-7" -> {
                List<StudentColor> temp1 = new ArrayList<>();
                List<StudentColor> temp2 = new ArrayList<>();

                temp1.add(parseStudentColorFromString(move.split(" ")[2]));
                if (!move.split(" ")[3].contains(nothing)) {
                    temp1.add(parseStudentColorFromString(move.split(" ")[3]));
                }
                if (!move.split(" ")[4].contains(nothing)) {
                    temp1.add(parseStudentColorFromString(move.split(" ")[4]));
                }
                parameters.add(temp1);

                temp2.add(parseStudentColorFromString(move.split(" ")[6]));
                if (!move.split(" ")[7].contains(nothing)) {
                    temp2.add(parseStudentColorFromString(move.split(" ")[7]));
                }
                if (!move.split(" ")[8].contains(nothing)) {
                    temp2.add(parseStudentColorFromString(move.split(" ")[8]));
                }
                parameters.add(temp2);
            }
            case "use-character-9", "use-character-11", "use-character-12" ->
                    parameters.add(parseStudentColorFromString(move.split(" ")[2]));
            case "use-character-10" -> {
                List<StudentColor> temp1 = new ArrayList<>();
                List<StudentColor> temp2 = new ArrayList<>();

                temp1.add(parseStudentColorFromString(move.split(" ")[2]));
                if (!move.split(" ")[3].contains(nothing)) {
                    temp1.add(parseStudentColorFromString(move.split(" ")[3]));
                }
                parameters.add(temp1);

                temp2.add(parseStudentColorFromString(move.split(" ")[5]));
                if (!move.split(" ")[6].contains(nothing)) {
                    temp2.add(parseStudentColorFromString(move.split(" ")[6]));
                }
                parameters.add(temp2);
            }
            default -> logger.log(Level.WARNING, "Received unsupported use-character move command");
        }
        doCharacterMove(id, parameters);
    }

    /**
     * get student color from string
     * @param color color text
     * @return student color
     */
    private StudentColor parseStudentColorFromString (String color) {
        return switch (color) {
            case "yellow" -> StudentColor.YELLOW;
            case "blue" -> StudentColor.BLUE;
            case "green" -> StudentColor.GREEN;
            case "red" -> StudentColor.RED;
            case "pink" -> StudentColor.PINK;
            default -> null;
        };
    }

    /**
     * print on the console the last warning or info and clear the last warning or info
     */
    private void printWarningOrHelper () {
        if (lastWarning != null) {
            printConsoleWarning(terminal, lastWarning);
            lastWarning = null;
        }
        if (lastHelper != null) {
            printCharacterHelper(terminal, lastHelper);
            lastHelper = null;
        }
    }

    /**
     * draw all the elements on the terminal
     */
    private void drawGameAndConsole () {
        final int baseCol = (terminal.getWidth() - 192) / 2;
        final int baseRow = 1;

        clearTerminal(terminal);

        terminal.writer().println(ansi().cursor(2, baseCol + 16).a("Dashboard").cursor(2, baseCol + 89).a("Eriantys Board").cursor(2, baseCol + 172).a("Players"));

        drawInfoSection(
                terminal,
                new Vector<>(Arrays.asList(baseRow + 4, baseCol + 6)),
                client.getIpAddress(),
                client.getPort(),
                client.getUsername(),
                client.getUsernames(),
                model
        );

        drawTilesAndClouds(
                terminal,
                new Vector<>(Arrays.asList(baseRow + 4, baseCol + 44)),
                model
        );

        drawPlayerBoards(
                terminal,
                new Vector<>(Arrays.asList(baseRow + 4, baseCol + 157)),
                model,
                client.getUsernames(),
                client.getUsernamesDisconnected()
        );

        drawConsoleArea(
                terminal,
                48
        );
    }

}

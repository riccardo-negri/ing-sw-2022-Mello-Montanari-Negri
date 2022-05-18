package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.client.page.ClientPage;
import it.polimi.ingsw.model.entity.characters.*;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.enums.GameMode;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.*;
import it.polimi.ingsw.networking.moves.Move;
import org.jline.reader.UserInterruptException;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.client.ui.cli.utils.BoardUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.CoreUtilsCLI.*;
import static it.polimi.ingsw.client.ui.cli.utils.ReadMoveUtilsCLI.*;
import static org.fusesource.jansi.Ansi.ansi;

public class BoardPageCLI extends AbstractBoardPage {
    private String lastWarning;
    private Integer lastHelper;
    volatile List<String> moveFromStdin = new ArrayList<>();

    public BoardPageCLI (Client client) {
        super(client);
    }

    /**
     * In short, here is all the controller of the CLI
     */
    @Override
    public void draw (Client client) {

        while (!client.getModel().isGameEnded() && client.getNextState() == ClientPage.BOARD_PAGE) { //TODO refactor
            try {
                drawGameAndConsole();
            } catch (ConcurrentModificationException e) {
                LOGGER.log(Level.WARNING, new StringBuilder().append("Got a ConcurrentModificationException while trying to draw the CLI: ").append(e).toString());
                drawGameAndConsole(); //TODO understand if there's a nicer way to handle this
            }

            if (lastWarning != null) {
                printConsoleWarning(terminal, lastWarning);
                lastWarning = null;
            }
            if(lastHelper != null) {
                printCharacterHelper(terminal, lastHelper);
                lastHelper = null;
            }
            LOGGER.log(Level.INFO, "Drew game board and console area");

            if (model.getGameState().getCurrentPlayer() == client.getUsernames().indexOf(client.getUsername())) { // enter if it's your turn

                moveFromStdin = new ArrayList<>();
                waitForMoveOrMessage();

                if (moveFromStdin.size() != 0) { // a move has have been read
                    LOGGER.log(Level.INFO, "Processing input move: " + moveFromStdin + " Game state: " + model.getGameState().getGameStateName());
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
                                case "MSS" -> doStudentMovement(getStudentColorFromString(moveFromStdin.get(0).split(" ")[1]), moveFromStdin.get(0).split(" ")[3]);
                                case "MMNS" -> doMotherNatureMovement(Integer.parseInt(moveFromStdin.get(0).split(" ")[2]));
                                case "CCS" -> doCloudChoice(Integer.parseInt(moveFromStdin.get(0).split(" ")[1]));
                                default -> {}
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Got an invalid move (that passed regex check), asking for it again. Exception: " + e);
                        lastWarning = "Please type a valid command. " + e.getMessage();
                        e.printStackTrace();
                        waitEnterPressed(terminal);
                    }
                }
                else { // no move was made, so it's a message about some disconnection events
                    Message message = client.getConnection().waitMessage();
                    handleMessageNotMoveAndPrintStatus(message);
                }
            }
            else { // enter here if it's not your turn
                printConsoleInfo(terminal, "Waiting for the other players to do a move...");
                Message message = client.getConnection().waitMessage();
                if (message instanceof Move) {
                    try {
                        applyOtherPlayersMove((Move) message);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Got an invalid move from the server. Exception: " + e);
                        e.printStackTrace();
                        waitEnterPressed(terminal);
                    }
                }
                else {
                    handleMessageNotMoveAndPrintStatus(message);
                }
            }
        }

        LOGGER.log(Level.FINE, "Game finished because someone won or surrendered");
        if (client.getNextState() == ClientPage.BOARD_PAGE) onEnd(false);
    }

    private void handleMessageNotMoveAndPrintStatus (Message message) {
        if (message instanceof Disconnected) {
            printConsoleWarning(terminal, "You got disconnected from the game, rejoin the game with the same username. Press enter to continue...");
            waitEnterPressed(terminal);
            onEnd(true);
        }
        else if (message instanceof UserDisconnected) {
            lastWarning = "Player " + ((UserDisconnected) message).getUsername() + " disconnected from the game.";
            client.getUsernamesDisconnected().add(((UserDisconnected) message).getUsername());
        }
        else if (message instanceof UserReconnected) {
            lastWarning = "Player " + ((UserReconnected) message).getUsername() + " reconnected to the game.";
            client.getUsernamesDisconnected().remove(((UserReconnected) message).getUsername());
        }
        else {
            lastWarning = "Received an unsupported message...";
        }

    }

    private void parseAndDoCharacterMove (String move) throws Exception {
        int ID = Integer.parseInt(move.split(" ")[0].split("-")[2]);
        ArrayList<Object> parameters = new ArrayList<>();
        switch (move.split(" ")[0]) { // adding parameters only where needed
            case "use-character-1" -> {
                parameters.add(getStudentColorFromString(move.split(" ")[2]));
                parameters.add(Integer.parseInt(move.split(" ")[4].split("-")[1]));
            }
            case "use-character-3", "use-character-5" -> parameters.add(Integer.parseInt(move.split(" ")[2].split("-")[1]));
            case "use-character-7" -> {
                List<StudentColor> temp1 = new ArrayList<>();
                List<StudentColor> temp2 = new ArrayList<>();

                temp1.add(getStudentColorFromString(move.split(" ")[2]));
                if (!move.split(" ")[3].contains("nothing")) {
                    temp1.add(getStudentColorFromString(move.split(" ")[3]));
                }
                if (!move.split(" ")[4].contains("nothing")) {
                    temp1.add(getStudentColorFromString(move.split(" ")[4]));
                }
                parameters.add(temp1);

                temp2.add(getStudentColorFromString(move.split(" ")[6]));
                if (!move.split(" ")[7].contains("nothing")) {
                    temp2.add(getStudentColorFromString(move.split(" ")[7]));
                }
                if (!move.split(" ")[8].contains("nothing")) {
                    temp2.add(getStudentColorFromString(move.split(" ")[8]));
                }
                parameters.add(temp2);
            }
            case "use-character-9", "use-character-11", "use-character-12" -> parameters.add(getStudentColorFromString(move.split(" ")[2]));
            case "use-character-10" -> {
                List<StudentColor> temp1 = new ArrayList<>();
                List<StudentColor> temp2 = new ArrayList<>();

                temp1.add(getStudentColorFromString(move.split(" ")[2]));
                if (!move.split(" ")[3].contains("nothing")) {
                    temp1.add(getStudentColorFromString(move.split(" ")[3]));
                }
                parameters.add(temp1);

                temp2.add(getStudentColorFromString(move.split(" ")[5]));
                if (!move.split(" ")[6].contains("nothing")) {
                    temp2.add(getStudentColorFromString(move.split(" ")[6]));
                }
                parameters.add(temp2);
            }
            default -> {}
        }
        doCharacterMove(ID, parameters);
    }

    private void askForMoveBasedOnState () {
        switch (model.getGameState().getGameStateName()) {
            case "PS" -> getMovePlayAssistant(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);
            case "MSS" -> getMoveStudentToIsland(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);
            case "MMNS" -> getMoveMotherNature(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);
            case "CCS" -> getMoveSelectCloud(terminal, commandsHistory, client.getUsername(), getCharactersID(), moveFromStdin);
            default -> {}
        }
    }

    private void waitForMoveOrMessage () {
        Thread t = new Thread(() -> {
            try {
                askForMoveBasedOnState();

            } catch (UserInterruptException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            }
        });

        client.getConnection().bindFunction(
                (Connection c) -> {
                    t.interrupt();
                    return false;
                }
        );

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int findRoundNumber () {
        if (model.getGameState().getGameStateName().equals("PS")) {
            return 11 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
        }
        return 10 - model.getWizard(model.getGameState().getCurrentPlayer()).getCardDeck().getDeckCards().length;
    }

    private StudentColor getStudentColorFromString (String color) {
        return switch (color) {
            case "yellow" -> StudentColor.YELLOW;
            case "blue" -> StudentColor.BLUE;
            case "green" -> StudentColor.GREEN;
            case "red" -> StudentColor.RED;
            case "pink" -> StudentColor.PINK;
            default -> null;
        };
    }

    private int[] getCharactersCost () {
        Character[] characters = model.getCharacters();
        int[] cost = new int[characters.length];
        for (int i = 0; i < characters.length; i++) {
            cost[i] = characters[i].getPrize();
        }
        return cost;
    }

    private int[] getCharactersID () {
        if (model.getGameMode().equals(GameMode.COMPLETE)) {
            Character[] characters = model.getCharacters();
            int[] id = new int[characters.length];
            for (int i = 0; i < characters.length; i++) {
                id[i] = characters[i].getId();
            }
            return id;
        }
        return new int[0];
    }

    private HashMap<Integer, int[]> getColorsListFromCharacters (Character[] characters) {
        HashMap<Integer, int[]> hashmap = new HashMap<>();
        for (int i = 0; i < characters.length; i++) {
            Character c = characters[i];
            if (c.getId() == 1) {
                hashmap.put(i, new int[]{
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                                (int) ((CharacterOne) c).getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        }
                );
            }
            else if (c.getId() == 7) {
                hashmap.put(i, new int[]{
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                                (int) ((CharacterSeven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        }
                );
            }
            else if (c.getId() == 11) {
                hashmap.put(i, new int[]{
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(2)).count(), // green
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(3)).count(), // red
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(0)).count(), // yellow
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(4)).count(), // pink
                                (int) ((CharacterEleven) c).getStudentColorList().stream().filter(s -> s.getValue().equals(1)).count(), // blue
                        }
                );
            }
            else {
                hashmap.put(i, new int[0]);
            }
        }
        return hashmap;
    }

    private int[] getNoEntryTilesFromCharacters (Character[] characters) {
        int[] array = new int[characters.length];
        for (int i = 0; i < characters.length; i++) {
            Character c = characters[i];
            if (c.getId() == 5) {
                array[i] = ((CharacterFive) c).getStopNumber();
            }
            else {
                array[i] = -1;
            }
        }
        return array;
    }

    private void drawGameAndConsole () {
        int baseCol = (terminal.getWidth() - 192) / 2;
        int baseRow = 1;

        clearTerminal(terminal);

        terminal.writer().println(ansi().cursor(2, baseCol + 16).a("Dashboard").cursor(2, baseCol + 89).a("Eriantys Board").cursor(2, baseCol + 172).a("Players"));

        drawInfoSection(
                terminal,
                baseRow + 4,
                baseCol + 6,
                client.getIPAddress(),
                client.getPort(),
                model.getGameMode().toString(),
                model.getPlayerNumber().getWizardNumber(),
                findRoundNumber(),
                client.getUsernames().get(model.getGameState().getCurrentPlayer()),
                model.getGameState().getGameStateName(),
                model.getGameMode().equals(GameMode.COMPLETE) ? getCharactersID() : null,
                model.getGameMode().equals(GameMode.COMPLETE) ? getCharactersCost() : null,
                model.getWizard(client.getUsernames().indexOf(client.getUsername())).getCardDeck().getDeckCards(),
                model.getGameMode().equals(GameMode.COMPLETE) ? getColorsListFromCharacters(model.getCharacters()) : null,
                model.getGameMode().equals(GameMode.COMPLETE) ? getNoEntryTilesFromCharacters(model.getCharacters()) : null
        );

        drawTilesAndClouds(
                terminal,
                baseRow + 4,
                baseCol + 44,
                model
        );

        drawPlayerBoards(
                terminal,
                baseRow + 4,
                baseCol + 157,
                model,
                client.getUsernames(),
                client.getUsername(),
                client.getUsernamesDisconnected()
        );

        drawConsoleArea(
                terminal,
                48);
    }

}

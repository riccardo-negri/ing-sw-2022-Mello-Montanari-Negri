package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ui.cli.CLI;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.networking.UserResigned;
import it.polimi.ingsw.networking.moves.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.client.page.ClientPage.*;

public abstract class AbstractBoardPage extends AbstractPage {

    protected AbstractBoardPage (Client client) {
        super(client);
    }

    public void doCardChoice (int card) throws Exception {
        Move moveToSend = new CardChoice(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), card);
        String toLog = new StringBuilder().append("Sending move CardChoice. Usernames: ").append(client.getUsernames()).append(". Your username: ").append(client.getUsername()).append(". Your ID: ").append(client.getUsernames().indexOf(client.getUsername())).toString();
        logger.log(Level.INFO, toLog);
        validateAndSendMove(moveToSend);
    }

    public void doStudentMovement (StudentColor color, String destination) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend;
        if (destination.contains("island")) {
            moveToSend = new IslandMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), color, Integer.parseInt(destination.split("-")[1]));
            String toLog = new StringBuilder().append("Sending move IslandMovement: ").append(color.toString()).append(" ").append(destination).toString();
            logger.log(Level.INFO, toLog);
        }
        else {
            moveToSend = new DiningRoomMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), color);
            String toLog = "Sending move DiningRoomMovement: " + color.toString() + " " + destination;
            logger.log(Level.INFO, toLog);
        }

        validateAndSendMove(moveToSend);
    }

    public void doMotherNatureMovement (int steps) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend = new MotherNatureMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), steps);
        String toLog = "Sending move MotherNatureMovement. Steps: " + steps;
        logger.log(Level.INFO, toLog);
        validateAndSendMove(moveToSend);
    }

    public void doCloudChoice (int id) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend = new CloudChoice(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), id);
        String toLog = "Sending move CloudChoice. Cloud: " + id;
        logger.log(Level.INFO, toLog);
        validateAndSendMove(moveToSend);
    }

    public void doCharacterMove (int characterID, List<Object> parameters) throws Exception {
        Move moveToSend;
        Wizard author = client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername()));
        String toLog;

        switch (characterID) {
            case 1 -> {
                moveToSend = new UseCharacter1(author, (StudentColor) parameters.get(0), (Integer) parameters.get(1));
                toLog = "Sending move UseCharacter1. Parameters: " + parameters.get(0).toString() + " " + parameters.get(1);

            }
            case 2 -> {
                moveToSend = new UseCharacter2(author);
                toLog = "Sending move UseCharacter2";
            }
            case 3 -> {
                moveToSend = new UseCharacter3(author, (Integer) parameters.get(0));
                toLog = "Sending move UseCharacter3. Parameters: " + ((Integer) parameters.get(0)).toString();
            }
            case 4 -> {
                moveToSend = new UseCharacter4(author);
                toLog = "Sending move UseCharacter4";
            }
            case 5 -> {
                moveToSend = new UseCharacter5(author, (Integer) parameters.get(0));
                toLog = "Sending move UseCharacter5. Parameters: " + parameters.get(0);
            }
            case 6 -> {
                moveToSend = new UseCharacter6(author);
                toLog = "Sending move UseCharacter6";
            }
            case 7 -> {
                moveToSend = new UseCharacter7(author, (List<StudentColor>) parameters.get(0), (List<StudentColor>) parameters.get(1));
                toLog = "Sending move UseCharacter7. Parameters: " + parameters.get(0) + " " + parameters.get(1);
            }
            case 8 -> {
                moveToSend = new UseCharacter8(author);
                toLog = "Sending move UseCharacter8";
            }
            case 9 -> {
                moveToSend = new UseCharacter9(author, (StudentColor) parameters.get(0));
                toLog = "Sending move UseCharacter9. Parameters: " + parameters.get(0).toString();
            }
            case 10 -> {
                moveToSend = new UseCharacter10(author, (List<StudentColor>) parameters.get(0), (List<StudentColor>) parameters.get(1));
                toLog = "Sending move UseCharacter10. Parameters: " + parameters.get(0) + " " + parameters.get(1);
            }
            case 11 -> {
                moveToSend = new UseCharacter11(author, (StudentColor) parameters.get(0));
                toLog = "Sending move UseCharacter11. Parameters: " + parameters.get(0).toString();
            }
            case 12 -> {
                moveToSend = new UseCharacter12(author, (StudentColor) parameters.get(0));
                toLog = "Sending move UseCharacter12. Parameters: " + parameters.get(0).toString();
            }
            default -> throw new IllegalArgumentException();
        }
        logger.log(Level.INFO, toLog);
        validateAndSendMove(moveToSend);

    }

    public void applyOtherPlayersMove (Move move) throws Exception {
        savePreviousState();
        String toLog = "Applying effects of the move just received. Move:" + move.toString();
        logger.log(Level.INFO, toLog);
        move.applyEffectClient(client.getModel());
    }

    private void validateAndSendMove (Move moveToSend) throws Exception {
        savePreviousState();

        String toLog = "Validating message. Move:" + moveToSend.toString();
        logger.log(Level.INFO, toLog);
        moveToSend.validate(client.getModel()); // may throw exception

        toLog = "Sending message after validation. Move:" + moveToSend;
        logger.log(Level.INFO, toLog);
        client.getConnection().send(moveToSend);

        // wait and apply message if the suer is using a CLI, in the GUI this is handled in a different way in the controller
        if (client.getUI() instanceof CLI) {
            toLog = "Waiting for message to come back. Move:" + moveToSend;
            logger.log(Level.INFO, toLog);
            Move moveToApply = (Move) client.getConnection().waitMessage(Move.class);

            toLog = "Applying effects of message. Move:" + moveToSend;
            logger.log(Level.INFO, toLog);
            moveToApply.applyEffectClient(client.getModel());

            toLog = "Applied effect of message. Move:" + moveToSend;
            logger.log(Level.INFO, toLog);
        }
    }

    private void savePreviousState () {
        logger.log(Level.INFO, "Saved state to file state.txt");
        try {
            try (FileWriter myWriter = new FileWriter("state_" + client.getUsername() + ".txt")) {
                myWriter.write(model.serializeGame());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onEnd (boolean gotDisconnected) {
        if (gotDisconnected) {
            client.setNextState(CONNECTION_PAGE);
        }
        else {
            client.setNextState(END_PAGE);
        }
    }

    /**
     * @param active true if the client decided to quit, false if someone else quit
     */
    public void onQuit(boolean active) {
        if (active) {
            UserResigned userResigned = new UserResigned(client.getUsername());
            client.getConnection().send(userResigned);
            logger.log(Level.INFO, "User Decided to resign from the game");
        }
        client.setNextState(MENU_PAGE);
    }
}

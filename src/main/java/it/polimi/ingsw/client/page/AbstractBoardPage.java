package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.entity.Wizard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.utils.moves.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static it.polimi.ingsw.client.page.ClientPage.CONNECTION_PAGE;
import static it.polimi.ingsw.client.page.ClientPage.END_PAGE;

public abstract class AbstractBoardPage extends AbstractPage {

    public AbstractBoardPage (Client client) {
        super(client);
    }

    public void doCardChoice (int card) throws Exception {
        Move moveToSend = new CardChoice(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), card);
        LOGGER.log(Level.INFO, "Sending move CardChoice. Usernames: " + client.getUsernames() + ". Your username: " + client.getUsername() + ". Your ID: " + client.getUsernames().indexOf(client.getUsername()));
        validateAndSendMove(moveToSend);
    }

    public void doStudentMovement (StudentColor color, String destination) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend;
        if (destination.contains("island")) {
            moveToSend = new IslandMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), color, Integer.parseInt(destination.split("-")[1]));
            LOGGER.log(Level.INFO, "Sending move IslandMovement: " + color.toString() + " " + destination);
        }
        else {
            moveToSend = new DiningRoomMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), color);
            LOGGER.log(Level.INFO, "Sending move DiningRoomMovement: " + color.toString() + " " + destination);
        }

        validateAndSendMove(moveToSend);
    }

    public void doMotherNatureMovement (int steps) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend = new MotherNatureMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), steps);
        LOGGER.log(Level.INFO, "Sending move MotherNatureMovement. Steps: " + steps);
        validateAndSendMove(moveToSend);
    }

    public void doCloudChoice (int id) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend = new CloudChoice(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), id);
        LOGGER.log(Level.INFO, "Sending move CloudChoice. Cloud: " + id);
        validateAndSendMove(moveToSend);
    }

    public void doCharacterMove (int characterID, ArrayList<Object> parameters) throws Exception {
        Move moveToSend;
        Wizard author = client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername()));
        switch (characterID) {
            case 1 -> {
                moveToSend = new UseCharacter1(author, (StudentColor) parameters.get(0), (Integer) parameters.get(1));
                LOGGER.log(Level.INFO, "Sending move UseCharacter1. Parameters: " + ((StudentColor) parameters.get(0)).toString() + " " + (Integer) parameters.get(1));
            }
            case 2 -> {
                moveToSend = new UseCharacter2(author);
                LOGGER.log(Level.INFO, "Sending move UseCharacter2");
            }
            case 3 -> {
                moveToSend = new UseCharacter3(author, (Integer) parameters.get(0));
                LOGGER.log(Level.INFO, "Sending move UseCharacter3. Parameters: " + ((Integer) parameters.get(0)).toString());
            }
            case 4 -> {
                moveToSend = new UseCharacter4(author);
                LOGGER.log(Level.INFO, "Sending move UseCharacter4");
            }
            case 5 -> {
                moveToSend = new UseCharacter5(author, (Integer) parameters.get(0));
                LOGGER.log(Level.INFO, "Sending move UseCharacter5. Parameters: " + (Integer) parameters.get(0));
            }
            case 6 -> {
                moveToSend = new UseCharacter6(author);
                LOGGER.log(Level.INFO, "Sending move UseCharacter6");
            }
            case 7 -> {
                moveToSend = new UseCharacter7(author, (List<StudentColor>) parameters.get(0), (List<StudentColor>) parameters.get(1));
                LOGGER.log(Level.INFO, "Sending move UseCharacter7. Parameters: " + (List<StudentColor>) parameters.get(0) + " " + (List<StudentColor>) parameters.get(1));
            }
            case 8 -> {
                moveToSend = new UseCharacter8(author);
                LOGGER.log(Level.INFO, "Sending move UseCharacter8");
            }
            case 9 -> {
                moveToSend = new UseCharacter9(author, (StudentColor) parameters.get(0));
                LOGGER.log(Level.INFO, "Sending move UseCharacter9. Parameters: " + ((StudentColor) parameters.get(0)).toString());
            }
            case 10 -> {
                moveToSend = new UseCharacter10(author, (List<StudentColor>) parameters.get(0), (List<StudentColor>) parameters.get(1));
                LOGGER.log(Level.INFO, "Sending move UseCharacter10. Parameters: " + (List<StudentColor>) parameters.get(0) + " " + (List<StudentColor>) parameters.get(1));
            }
            case 11 -> {
                moveToSend = new UseCharacter11(author, (StudentColor) parameters.get(0));
                LOGGER.log(Level.INFO, "Sending move UseCharacter11. Parameters: " + ((StudentColor) parameters.get(0)).toString());
            }
            case 12 -> {
                moveToSend = new UseCharacter12(author, (StudentColor) parameters.get(0));
                LOGGER.log(Level.INFO, "Sending move UseCharacter12. Parameters: " + ((StudentColor) parameters.get(0)).toString());
            }
            default -> moveToSend = null;
        }
        validateAndSendMove(moveToSend);
    }

    public void applyOtherPlayersMove (Move move) throws Exception {
        savePreviousState();

        LOGGER.log(Level.INFO, "Applying effects of the move just received. Move:" + move.toString());
        move.applyEffectClient(client.getModel());
    }

    private void validateAndSendMove (Move moveToSend) throws Exception {
        savePreviousState();

        LOGGER.log(Level.INFO, "Validating message. Move:" + moveToSend.toString());
        moveToSend.validate(client.getModel()); // may throw exception

        LOGGER.log(Level.INFO, "Sending message after validation. Move:" + moveToSend.toString());
        client.getConnection().send(moveToSend);

        LOGGER.log(Level.INFO, "Waiting for message to come back. Move:" + moveToSend.toString());
        Move moveToApply = (Move) client.getConnection().waitMessage(Move.class);

        LOGGER.log(Level.INFO, "Applying effects of message. Move:" + moveToSend.toString());
        moveToApply.applyEffectClient(client.getModel());

        LOGGER.log(Level.INFO, "Applied effect of message. Move:" + moveToSend.toString());
    }

    private void savePreviousState () {
        LOGGER.log(Level.INFO, "Saved state to file state.txt");
        try {
            FileWriter myWriter = new FileWriter("state_" + client.getUsername() + ".txt");
            myWriter.write(model.serializeGame());
            myWriter.close();
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
}

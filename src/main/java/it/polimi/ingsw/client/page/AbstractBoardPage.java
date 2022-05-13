package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.enums.AssistantCard;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.utils.Disconnected;
import it.polimi.ingsw.utils.Redirect;
import it.polimi.ingsw.utils.moves.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static it.polimi.ingsw.client.page.ClientState.START_PAGE;
import static it.polimi.ingsw.client.page.ClientState.WELCOME_PAGE;

public abstract class AbstractBoardPage extends AbstractClientState {

    public AbstractBoardPage (Client client) {
        super(client);
    }

    public void doCardChoice (int card) throws Exception {
        Move moveToSend = new CardChoice(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), card);
        client.getLogger().log(Level.INFO, "Sending move CardChoice. Usernames: " + client.getUsernames() + ". Your username: " + client.getUsername() + ". Your ID: " + client.getUsernames().indexOf(client.getUsername()));
        validateAndSendMove(moveToSend);
    }

    public void doStudentMovement (StudentColor color, String destination) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend;
        if (destination.contains("island")) {
            moveToSend = new IslandMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), color, Integer.parseInt(destination.split("-")[1]));
            client.getLogger().log(Level.INFO, "Sending move IslandMovement: " + color.toString() + " " + destination);
        }
        else {
            moveToSend = new DiningRoomMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), color);
            client.getLogger().log(Level.INFO, "Sending move DiningRoomMovement: " + color.toString() + " " + destination);
        }

        validateAndSendMove(moveToSend);
    }

    public void doMotherNatureMovement (int steps) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend = new MotherNatureMovement(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), steps);
        client.getLogger().log(Level.INFO, "Sending move MotherNatureMovement. Steps: " + steps);
        validateAndSendMove(moveToSend);
    }

    public void doCloudChoice (int id) throws Exception { // destination can either be "dining-room" or "island-ID"
        Move moveToSend = new CloudChoice(client.getModel().getWizard(client.getUsernames().indexOf(client.getUsername())), id);
        client.getLogger().log(Level.INFO, "Sending move CloudChoice. Cloud: " + id);
        validateAndSendMove(moveToSend);
    }

    public void applyOtherPlayersMove (Move move) throws Exception {
        move.applyEffectClient(client.getModel());
    }

    private void validateAndSendMove (Move moveToSend) throws Exception {
        client.getLogger().log(Level.FINEST, "Validating message. Move:" + moveToSend.toString());
        moveToSend.validate(client.getModel()); // may throw exception

        client.getLogger().log(Level.FINEST, "Sending message after validation. Move:" + moveToSend.toString());
        client.getConnection().send(moveToSend);

        client.getLogger().log(Level.FINEST, "Waiting for message to come back. Move:" + moveToSend.toString());
        Move moveToApply = (Move) client.getConnection().waitMessage(Move.class);

        client.getLogger().log(Level.FINEST, "Applying effects of message. Move:" + moveToSend.toString());
        moveToApply.applyEffectClient(client.getModel());

        client.getLogger().log(Level.FINEST, "Applied effect of message. Move:" + moveToSend.toString());
    }

    public Class waitAndHandleMessage () {
        return Disconnected.class;

    }

    public void doDiningRoomMovement () {

    }

    public void onEnd () {
        client.setNextState(WELCOME_PAGE);
    }
}

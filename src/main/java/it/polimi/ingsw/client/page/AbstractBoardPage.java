package it.polimi.ingsw.client.page;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.enums.AssistantCard;
import it.polimi.ingsw.utils.Disconnected;
import it.polimi.ingsw.utils.Redirect;
import it.polimi.ingsw.utils.moves.CardChoice;
import it.polimi.ingsw.utils.moves.Move;

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

    public void applyOtherPlayersMove (Move move) throws Exception {
        move.applyEffectClient(client.getModel());
    }

    private void validateAndSendMove (Move moveToSend) throws Exception {
        System.out.println("Validating message");
        moveToSend.validate(client.getModel()); // may throw exception

        System.out.println("Sending message after validation");
        client.getConnection().send(moveToSend);

        System.out.println("Waiting for message to come back");
        CardChoice moveToApply = (CardChoice) client.getConnection().waitMessage(CardChoice.class);

        System.out.println("Applying effects of message");
        moveToApply.applyEffectClient(client.getModel());
        //moveToSend.applyEffectClient(client.getModel());

        System.out.println("Applied effect of message");
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

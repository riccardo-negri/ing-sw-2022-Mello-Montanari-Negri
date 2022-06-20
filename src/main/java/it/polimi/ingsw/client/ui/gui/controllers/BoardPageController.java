package it.polimi.ingsw.client.ui.gui.controllers;

import it.polimi.ingsw.client.page.AbstractBoardPage;
import it.polimi.ingsw.model.enums.AssistantCard;
import it.polimi.ingsw.networking.Connection;
import it.polimi.ingsw.networking.Message;
import it.polimi.ingsw.networking.UserResigned;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;

public class BoardPageController extends AbstractController{
    @FXML
    ImageView card1;
    @FXML
    ImageView card2;
    @FXML
    ImageView card3;
    @FXML
    ImageView card4;
    @FXML
    ImageView card5;
    @FXML
    ImageView card6;
    @FXML
    ImageView card7;
    @FXML
    ImageView card8;
    @FXML
    ImageView card9;
    @FXML
    ImageView card10;

    List<ImageView> cards;

    @FXML
    ImageView wizard0;
    @FXML
    ImageView wizard1;
    @FXML
    ImageView wizard2;
    @FXML
    ImageView wizard3;

    List<ImageView> wizards;

    @FXML
    ImageView assistant0;
    @FXML
    ImageView assistant1;
    @FXML
    ImageView assistant2;
    @FXML
    ImageView assistant3;

    List<ImageView> assistants;

    @FXML
    private void handleResign(ActionEvent event) {
        client.getConnection().send(new UserResigned(client.getUsername()));
    }

    @FXML
    void initialize() {
        client.getConnection().bindFunctionAndTestPrevious(this::onNewMessage);

        // get the elements
        cards = Arrays.asList(card1, card2, card3, card4, card5, card6, card7, card8, card9, card10);
        wizards = Arrays.asList(wizard0, wizard1, wizard2, wizard3);
        assistants = Arrays.asList(assistant0, assistant1, assistant2, assistant3);

        updateBoard();
    }

    boolean onNewMessage(Connection source) {
        Message m = source.getFirstMessage();
        if (m instanceof UserResigned ur) {
            client.setResigned(ur.getUsername());
            ((AbstractBoardPage) client.getCurrState()).onEnd(false);
            client.getConnection().close();
            Platform.runLater(()-> client.drawNextPage());
            return true;
        }
        return false;
    }

    void updateBoard() {

        // display only the correct amount of wizards and assistants
        for(int i = 4; i > client.getUsernames().size(); i--) {
            wizards.get(i-1).setVisible(false);
            assistants.get(i-1).setVisible(false);
        }

        // hide assistants initially
        int otherPlayersIndex = 1;
        for(String user : client.getUsernames()) {
            int currId = client.getUsernames().indexOf(user);
            AssistantCard currCard = client.getModel().getWizard(currId).getCardDeck().getCurrentCard();

            if(user.equals(client.getUsername())) {
                if (currCard == null) {
                    assistant0.setVisible(false);
                }
                else {
                    Image image = new Image("assets/cards/assistants/" + currCard.getNumber() + ".png");
                    assistant0.setImage(image);
                }
            }
            else {
                if (currCard == null) {
                    assistants.get(otherPlayersIndex).setVisible(false);
                }
                else {
                    Image image = new Image("assets/cards/assistants/" + currCard.getNumber() + ".png");
                    assistants.get(otherPlayersIndex).setImage(image);
                }
                otherPlayersIndex += 1;
            }
        }

    }
}
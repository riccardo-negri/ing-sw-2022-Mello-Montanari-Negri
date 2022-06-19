package it.polimi.ingsw.networking.moves;

import it.polimi.ingsw.model.entity.Wizard;

public abstract class UseCharacter extends Move {

    /**
     * create a message requesting to activate a character
     * @param author the id of the wizard who required the move
     */
    protected UseCharacter(Wizard author) {
        super(author);
    }
}

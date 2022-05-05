package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.PlayerNumber;
import it.polimi.ingsw.model.enums.StudentColor;
import it.polimi.ingsw.model.enums.Tower;

import java.lang.management.PlatformLoggingMXBean;
import java.util.List;

/**
 * class to represent the player
 */
public class Wizard {
    private final Integer wizardId;
    private Integer money;
    private final CardDeck cardDeck;
    private final List<StudentColor> entranceStudents;
    private final Integer[] diningStudents;
    private final Tower towerColor;
    private Integer towerNumber;

    /**
     * create the wizard
     * @param entranceStudents list of students at the beginning of the game, taken from the bag
     * @param towerColor color of the player
     * @param towerNumber starting number of towers, depending on the number of players
     */
    public Wizard(int wizardId, List<StudentColor> entranceStudents, Tower towerColor, Integer towerNumber) {
        this.wizardId = wizardId;
        this.towerNumber = towerNumber;
        cardDeck = new CardDeck();
        money = 1;
        this.entranceStudents = entranceStudents;
        this.towerColor = towerColor;
        diningStudents = new Integer[5];
        for (int i=0; i<5; i++)
            diningStudents[i] = 0;
    }

    public CardDeck getCardDeck() { return cardDeck; }

    public void takeEntranceStudent (StudentColor studentColor) { entranceStudents.remove(studentColor); }

    public void putDiningStudent (StudentColor studentColor) { if(++diningStudents[studentColor.getValue()] % 3 == 0) money++; }

    public void takeDiningStudent (StudentColor studentColor) { diningStudents[studentColor.getValue()] -= diningStudents[studentColor.getValue()]>0 ? 1 : 0; }

    public void changeTowerNumber(Integer difference) {
        towerNumber += difference;
        if(towerNumber <= 0) {
            //TODO end game condition
        }
    }

    public void payEffect(Integer price) throws Exception{
        if (price > money) throw new Exception("Not enough money to activate the effect");
        money -= price;
    }

    public Integer getMoney() { return money; }

    public Integer getDiningStudents (StudentColor studentColor) { return diningStudents[studentColor.getValue()]; }

    public Tower getTowerColor() { return towerColor; }

    public List<StudentColor> getEntranceStudents() { return entranceStudents; }

    public Integer getId() { return wizardId; }
}







package it.polimi.ingsw.model.entity;

import it.polimi.ingsw.model.enums.StudentColor;

import java.util.Arrays;

/**
 * Assistant card, to be deleted from the list after being used
 */
public enum AssistantCard {
    ONE(1,1),
    TWO(2,1),
    THREE(3,2),
    FOUR(4,2),
    FIVE(5,3),
    SIX(6,3),
    SEVEN(7,4),
    EIGHT(8,4),
    NINE(9,5),
    TEN(10,5);
    private final Integer number;
    private final Integer steps;

    /**
     * the constructor only requires the card number, the number of steps is calculated from it
     * @param number card number, can't be changed at runtime
     */
    AssistantCard(Integer number, Integer step) {
        this.number = number;
        this.steps = step;
    }

    public Integer getNumber() { return this.number; }

    public Integer getSteps() { return steps; }

    public static AssistantCard getFromValue(int value) {
        for (AssistantCard assistantCard : values()) {
            if (assistantCard.number == value) return assistantCard;
        }
        return null;
    }
}

package model.entity;

/**
 * Assistant card, to be deleted from the list after being used
 */
public class AssistantCard {
    private final Integer number;
    private final Integer steps;

    /**
     * the constructor only requires the card number, the number of steps is calculated from it
     * @param number card number, can't be changed at runtime
     */
    public AssistantCard(Integer number) {
        this.number = number;
        this.steps = (int) Math.ceil((double) number / 2);
    }

    public Integer getNumber() { return this.number; }

    public Integer getSteps() { return steps; }
}

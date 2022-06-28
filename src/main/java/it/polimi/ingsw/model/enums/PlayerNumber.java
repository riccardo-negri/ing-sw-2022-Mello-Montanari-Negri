package it.polimi.ingsw.model.enums;

/**
 * contains a lot of parameters that depend on the number of players to avoid repetitions around the code
 */
public enum PlayerNumber {
    TWO(2,3, 7,8),
    THREE(3,4, 9,6),
    FOUR(4,3, 7,8);

    private final Integer wizardNumber;
    private final Integer cloudSize;
    private final Integer entranceNumber;
    private final Integer towerNumber;

    PlayerNumber(Integer wizardNumber, Integer cloudSize, Integer entranceNumber, Integer towerNumber) {
        this.wizardNumber = wizardNumber;
        this.cloudSize = cloudSize;
        this.entranceNumber = entranceNumber;
        this.towerNumber = towerNumber;
    }

    public Integer getWizardNumber() {
        return wizardNumber;
    }

    public Integer getCloudSize() {
        return cloudSize;
    }

    public Integer getEntranceNumber() { return entranceNumber; }

    public static PlayerNumber fromNumber (int value) {
        for (PlayerNumber s : values()) {
            if (s.getWizardNumber() == value) return s;
        }
        return null;
    }

    public Integer getTowerNumber() { return towerNumber; }
}

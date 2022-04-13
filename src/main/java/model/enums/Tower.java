package model.enums;

public enum Tower {
    BLACK(0),
    WHITE(1),
    GRAY(2);


    private final Integer value;

    Tower (Integer value) {
        this.value = value;
    }

    public static Tower fromNumber (int value) {
        for (Tower t : values()) {
            if (t.value == value) return t;
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }
}

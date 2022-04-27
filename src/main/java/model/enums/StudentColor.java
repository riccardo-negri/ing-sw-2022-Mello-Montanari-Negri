package model.enums;

public enum StudentColor {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);

    public final Integer value;

    StudentColor(Integer value) { this.value = value; }

    public Integer getValue() {
        return value;
    }

    public static StudentColor fromNumber (int value) {
        for (StudentColor s : values()) {
            if (s.value == value) return s;
        }
        return null;
    }
}

package enums;

public enum Emotion {
    HAPPY("счастлив"),

    SAD("грустный"),

    ANGRY("злой"),

    NETUTRAL("нейтральный"),

    SORRY("жалеет"),

    ENVY("завидует"),

    HOPE("надеется");

    private final String description;

    Emotion(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}

package records;

public record Job(String title, int salary) {
    public static final Job EMPLOYED = new Job("Продавец", 50);
    public static final Job UNEMPLOYED = new Job("Безработный", 0);
}

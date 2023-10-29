package Objects;

public enum FloorButtonStatus {
    ON("I"),
    OFF("O");

    private final String title;

    FloorButtonStatus(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
